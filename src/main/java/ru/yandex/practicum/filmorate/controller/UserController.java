package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Error;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Валидация добавления пользователя");
        if (isBlank(user.getName())) {
            log.info("Имя не введено. Вместо него используется логин - {}", user.getLogin());
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        log.debug("Пользователю присвоен id {}", user.getId());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен со следующими параметрами - {}", users.get(user.getId()));
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            throw new ValidationException("Пользователь с id " + newUser.getId() + " не найден");
        }
        log.debug("Проверка введенных параметров пользователя на null");
        userUpdateNullChecker(newUser, oldUser);
        log.debug("Валидация обновления пользователя");
        oldUser.setEmail(newUser.getEmail());
        oldUser.setName(newUser.getName());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        log.debug("Информация о пользователе обновлена. Новые параметры пользователя - {}", users.get(oldUser.getId()));
        return oldUser;
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void userUpdateNullChecker(User newUser, User oldUser) {
        if (newUser.getEmail() == null) {
            log.debug("При обновлении пользователя не был введен email. Поле не обновляется, прежнее значение - {}",
                    oldUser.getEmail());
            newUser.setEmail(oldUser.getEmail());
        }
        if (newUser.getLogin() == null) {
            log.debug("При обновлении пользователя не был введен логин. Поле не обновляется, прежнее значение - {}",
                    oldUser.getLogin());
            newUser.setLogin(oldUser.getLogin());
        }
        if (newUser.getName() == null) {
            log.debug("При обновлении пользователя не было введено имя. Поле не обновляется, прежнее значение - {}",
                    oldUser.getName());
            newUser.setName(oldUser.getName());
        }
        if (newUser.getBirthday() == null) {
            log.debug("При обновлении пользователя не была введена дата рождения. Поле не обновляется," +
                    " прежнее значение - {}", oldUser.getBirthday());
            newUser.setBirthday(oldUser.getBirthday());
        }
    }

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Error> handleValidationException(Throwable e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
