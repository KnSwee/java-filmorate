package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.mapper.FriendListMapper;
import ru.yandex.practicum.filmorate.storage.user.mapper.MutualFriendsMapper;
import ru.yandex.practicum.filmorate.storage.user.mapper.UserRowMapper;

import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper;
    private final FriendListMapper friendsMapper;
    private final MutualFriendsMapper mutualFriendsMapper;
    private static final String ENTITY_NOT_FOUND = "Пользователь с id %s не найден";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthdate = ? WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT u.*, f.FRIEND_ADDED_ID FROM users u LEFT JOIN user_friend f ON u.id = f.user_id;";
    private static final String FIND_BY_ID_QUERY = "SELECT u.*, f.FRIEND_ADDED_ID FROM users u LEFT JOIN user_friend f ON u.id = f.user_id WHERE id = ?;";
    private static final String ADD_NEW_FRIEND_QUERY = "INSERT INTO user_friend(user_id, friend_added_id) VALUES (?, ?);";
    private static final String GET_FRIENDS_QUERY = "SELECT user_id AS id FROM user_friend WHERE friend_added_id = ?;";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM user_friend WHERE user_id = ? AND friend_added_id = ?;";
    private static final String GET_MUTUAL_FRIENDS_QUERY = "SELECT uf1.user_id AS mutual_friends\n" +
            "FROM user_friend uf1 JOIN user_friend uf2 ON uf1.user_id = uf2.user_id\n" +
            "WHERE uf1.friend_added_id = ? AND uf2.friend_added_id = ?;";

    @Override
    public User create(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());
        userMap.put("login", user.getLogin());
        userMap.put("name", user.getName());
        userMap.put("birthdate", user.getBirthday());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Long userId = simpleJdbcInsert.executeAndReturnKey(userMap).longValue();
        return getUser(userId);
    }

    @Override
    public User update(User updatingUser) {
        int rowsUpdated = jdbc.update(UPDATE_USER_QUERY, updatingUser.getEmail(), updatingUser.getLogin(), updatingUser.getName(), updatingUser.getBirthday(), updatingUser.getId());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return updatingUser;
    }

    @Override
    public Collection<User> findAll() {
        return Optional.of(jdbc.query(FIND_ALL_QUERY, mapper).getFirst().values())
                .orElseThrow(() -> new EntityNotFoundException("Не удалось получить пользователей"));
    }

    public User getUser(Long id) {
        if (jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id) == null) {
            throw new EntityNotFoundException(ENTITY_NOT_FOUND.formatted(id));
        }
        return jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id).get(id);
    }

    public void addFriend(Long userId, Long friendId) {
        jdbc.update(ADD_NEW_FRIEND_QUERY, friendId, userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        jdbc.update(DELETE_FRIEND_QUERY, friendId, userId);
    }

    public List<Long> getFriends(Long id) {
        List<ArrayList<Long>> query = jdbc.query(GET_FRIENDS_QUERY, friendsMapper, id);
        if (!query.isEmpty()) {
            return query.getFirst();
        }
        return new ArrayList<>();

    }

    public List<Long> getMutualFriends(Long userId, Long anotherUserId) {
        List<List<Long>> friends = jdbc.query(GET_MUTUAL_FRIENDS_QUERY, mutualFriendsMapper, anotherUserId, userId);
        if (friends.isEmpty()) {
            return new ArrayList<>();
        }
        return friends.getFirst();
    }

}
