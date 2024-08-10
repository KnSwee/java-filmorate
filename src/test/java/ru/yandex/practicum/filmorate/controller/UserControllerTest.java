package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserControllerTest {

    UserController users = new UserController();

    private static User.UserBuilder getValidUser() {
        return User.builder()
                .id(1)
                .email("example@example.com")
                .login("login")
                .name("имя")
                .birthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    public void shouldNotCreateUserWithIncorrectEmail() {
        User blankUser = getValidUser().email("").build();
        User nullUser = getValidUser().email(null).build();
        User withoutAtUser = getValidUser().email("exampleexample.com").build();

        assertThrows(ValidationException.class, () -> users.create(blankUser));
        assertThrows(ValidationException.class, () -> users.create(nullUser));
        assertThrows(ValidationException.class, () -> users.create(withoutAtUser));
    }

    @Test
    public void shouldNotCreateUserWithIncorrectLogin() {
        User blankUser = getValidUser().login("").build();
        User nullUser = getValidUser().login(null).build();
        User spaceUser = getValidUser().login("login login").build();

        assertThrows(ValidationException.class, () -> users.create(blankUser));
        assertThrows(ValidationException.class, () -> users.create(nullUser));
        assertThrows(ValidationException.class, () -> users.create(spaceUser));
    }

    @Test
    public void shouldUseLoginInsteadOfNameIfNameIsBlank() {
        User blankUser = users.create(getValidUser().name("").build());
        User nullUser = users.create(getValidUser().name(null).build());

        assertEquals(blankUser.getLogin(), blankUser.getName());
        assertEquals(nullUser.getLogin(), nullUser.getName());
    }

    @Test
    public void shouldNotCreateUserWithIncorrectBirthday() {
        User fromFutureUser = getValidUser().birthday(LocalDate.of(2077, 1, 1)).build();

        assertThrows(ValidationException.class, () -> users.create(fromFutureUser));
    }

    @Test
    public void shouldUpdateNullUser() {
        User user = users.create(getValidUser().build());

        User nullUser = users.update(getValidUser().login(null).email(null).birthday(null).name(null).build());

        assertEquals(user, nullUser);
    }

}
