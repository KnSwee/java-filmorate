package ru.yandex.practicum.filmorate.storageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@JdbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
class UserStorageTest {
    private final UserDbStorage userStorage;

    @BeforeAll
    public static void beforeAll() {

    }

    @Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void testFindUserById() {
        User user = userStorage.getUser(1L); // Получите пользователя напрямую
        assertThat(user).isNotNull(); // Проверьте, что результат не null
        assertThat(user).hasFieldOrPropertyWithValue("id", 1L); // Проверка значения id
    }

    @Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void createUserTest() {
        User user = new User(null, "exapmle@example.com", "newlogin", "newname",
                LocalDate.of(1998, 9, 1), new HashSet<>());
        Long id = userStorage.create(user).getId();
        user.setId(id);
        User dbUser = userStorage.getUser(id);

        assertEquals(user, dbUser);
        assertEquals(user.getEmail(), dbUser.getEmail());
        assertEquals(user.getLogin(), dbUser.getLogin());
        assertEquals(user.getName(), dbUser.getName());
        assertEquals(user.getBirthday(), dbUser.getBirthday());
    }

    @Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void updateUserTest() {
        User user = new User(null, "exapmle@example.com", "newlogin", "newname",
                LocalDate.of(1998, 9, 1), new HashSet<>());
        Long id = userStorage.create(user).getId();
        user.setId(id);
        User updUser = new User(id, "upd@upd.com", "updLogin", "updName",
                LocalDate.of(2077, 12, 12), new HashSet<>());

        userStorage.update(updUser);
        User dbUser = userStorage.getUser(id);

        assertEquals(updUser, dbUser);
        assertEquals(updUser.getEmail(), dbUser.getEmail());
        assertEquals(updUser.getLogin(), dbUser.getLogin());
        assertEquals(updUser.getName(), dbUser.getName());
        assertEquals(updUser.getBirthday(), dbUser.getBirthday());
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void findAllTest() {
        User user = new User(null, "exapmle@example.com", "newlogin", "newname",
                LocalDate.of(1998, 9, 1), new HashSet<>());
        User user2 = new User(null, "exapmle2@example.com", "newlogin2", "newname2",
                LocalDate.of(1992, 3, 5), new HashSet<>());
        userStorage.create(user);
        userStorage.create(user2);

        assertEquals(2, userStorage.findAll().size());
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void addGetDeleteFriendsTest() {
        User user = new User(null, "exapmle@example.com", "newlogin", "newname",
                LocalDate.of(1998, 9, 1), new HashSet<>());
        User user2 = new User(null, "exapmle2@example.com", "newlogin2", "newname2",
                LocalDate.of(1992, 3, 5), new HashSet<>());
        Long userId = userStorage.create(user).getId();
        Long user2Id = userStorage.create(user2).getId();

        userStorage.addFriend(userId, user2Id);

        assertEquals(1, userStorage.getFriends(userId).size());
        assertEquals(0, userStorage.getFriends(user2Id).size());

        userStorage.deleteFriend(userId, user2Id);

        assertEquals(0, userStorage.getFriends(userId).size());
        assertEquals(0, userStorage.getFriends(user2Id).size());
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void getMutualFriends() {
        User user = new User(null, "exapmle@example.com", "newlogin", "newname",
                LocalDate.of(1998, 9, 1), new HashSet<>());
        User user2 = new User(null, "exapmle2@example.com", "newlogin2", "newname2",
                LocalDate.of(1992, 3, 5), new HashSet<>());
        User user3 = new User(null, "exapmle3@example.com", "newlogin3", "newname3",
                LocalDate.of(1993, 4, 6), new HashSet<>());
        Long userId = userStorage.create(user).getId();
        Long user2Id = userStorage.create(user2).getId();
        Long user3Id = userStorage.create(user3).getId();

        assertEquals(0, userStorage.getMutualFriends(userId, user2Id).size());

        userStorage.addFriend(userId, user3Id);
        userStorage.addFriend(user2Id, user3Id);

        assertEquals(1, userStorage.getMutualFriends(userId, user2Id).size());
    }








}