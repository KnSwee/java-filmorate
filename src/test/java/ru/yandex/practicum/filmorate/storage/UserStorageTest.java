package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserStorageTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    InMemoryUserStorage users = new InMemoryUserStorage();

    private static User.UserBuilder getValidUser() {
        return User.builder()
                .id(1L)
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
        User withoutDomainUser = getValidUser().email("example@example").login("biba boba").build();

        Set<ConstraintViolation<User>> violations;
        violations = validator.validate(blankUser);
        assertFalse(violations.isEmpty());
        violations = validator.validate(nullUser);
        assertFalse(violations.isEmpty());
        violations = validator.validate(withoutAtUser);
        assertFalse(violations.isEmpty());
        violations = validator.validate(withoutDomainUser);
        assertFalse(violations.isEmpty());

    }

    @Test
    public void shouldNotCreateUserWithIncorrectLogin() {
        User blankUser = getValidUser().login("").build();
        User nullUser = getValidUser().login(null).build();
        User spaceUser = getValidUser().login("login login").build();

        Set<ConstraintViolation<User>> violations;
        violations = validator.validate(blankUser);
        assertFalse(violations.isEmpty());
        violations = validator.validate(nullUser);
        assertFalse(violations.isEmpty());
        violations = validator.validate(spaceUser);
        assertFalse(violations.isEmpty());
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

        Set<ConstraintViolation<User>> violations;
        violations = validator.validate(fromFutureUser);
        assertFalse(violations.isEmpty());
    }

}
