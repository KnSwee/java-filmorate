package ru.yandex.practicum.filmorate.storage.user.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserRowMapper implements RowMapper<Map<Long, User>> {
    @Override
    public Map<Long, User> mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Map<Long, User> map = new HashMap<>();

        do {
            Long userId = resultSet.getLong("id");
            User iterUser = map.getOrDefault(userId, new User());
            if (iterUser.getId() == null) {
                iterUser.setName(resultSet.getString("name"));
                iterUser.setId(resultSet.getLong("id"));
                if (resultSet.getDate("birthdate") != null) {
                    iterUser.setBirthday(resultSet.getDate("birthdate").toLocalDate());
                }
                iterUser.setLogin(resultSet.getString("login"));
                iterUser.setEmail(resultSet.getString("email"));
            }
            long friendId = resultSet.getLong("friend_added_id");
            if (friendId != 0) {
                iterUser.getFriends().add(friendId);
            }
            map.put(userId, iterUser);
        } while (resultSet.next());
        return map;
    }
}
