package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Поступил объект на создание пользователя, User - {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Поступил объект на обновление пользователя, newUser - {}", newUser);
        return userService.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Long addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        log.info("Попытка добавить друга с id {} пользователем с id {}", friendId, id);
        return userService.addFriend(id, friendId);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрос на вывод всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Запрос на вывод пользователя с id {}", id);
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {
        log.info("Запрос на вывод друзей пользователя с id {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Long id,
                                             @PathVariable Long otherId) {
        log.info("Запрос на вывод общих друзей пользователей c id {} и otherId {}", id, otherId);
        return userService.getMutualFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Long deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        log.info("Запрос на удаление друга с id {} у пользователя с id {}", friendId, id);
        return userService.deleteFriend(id, friendId);
    }

}
