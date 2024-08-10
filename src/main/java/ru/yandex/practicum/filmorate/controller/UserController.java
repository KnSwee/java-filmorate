package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user) {
        log.debug("Валидация добавления пользователя");
        userValidation(user);
        user.setId(getNextId());
        log.debug("Пользователю присвоен id {}", user.getId());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен со следующими параметрами - {}", users.get(user.getId()));
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        User oldUser = users.get(newUser.getId());
        log.debug("Проверка введенных параметров пользователя на null");
        userUpdateNullChecker(newUser, oldUser);
        log.debug("Валидация обновления пользователя");
        userValidation(newUser);
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

    private void userValidation(User user) {
        if (isBlank(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("Был введен некорректный email - {}", user.getEmail());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (isBlank(user.getLogin()) || user.getLogin().contains(" ")) {
            log.error("Был введен пустой логин или логин с пробелами - {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (isBlank(user.getName())) {
            log.info("Имя не введено. Вместо него используется логин - {}", user.getLogin());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Была введена неверная дата рождения. Сейчас {}, Была введена дата рождения {}",
                    LocalDate.now(), user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
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
}
