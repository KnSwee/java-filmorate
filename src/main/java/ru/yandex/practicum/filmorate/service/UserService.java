package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.mapper.UserDtoMapper;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDbStorage storage;

    public Long addFriend(Long userId, Long friendId) {
        if (!storage.findAll().stream().map(User::getId).toList().contains(userId)) {
            throw new EntityNotFoundException("Пользователя с Id %d не существует".formatted(userId));
        }
        if (!storage.findAll().stream().map(User::getId).toList().contains(friendId)) {
            throw new EntityNotFoundException("Пользователя с Id %d не существует".formatted(friendId));
        }
        storage.addFriend(userId, friendId);
        return friendId;
    }

    public Long deleteFriend(Long userId, Long friendId) {
        if (!storage.findAll().stream().map(User::getId).toList().contains(userId)) {
            throw new EntityNotFoundException("Пользователя с Id %d не существует".formatted(userId));
        }
        if (!storage.findAll().stream().map(User::getId).toList().contains(friendId)) {
            throw new EntityNotFoundException("Пользователя с Id %d не существует".formatted(friendId));
        }
        storage.deleteFriend(userId, friendId);
        return friendId;
    }

    public Collection<UserDto> getFriends(Long userId) {
        if (!storage.findAll().stream().map(User::getId).toList().contains(userId)) {
            throw new EntityNotFoundException("Пользователя с Id %d не существует".formatted(userId));
        }
        return getUsers(storage.getFriends(userId));
    }

    public UserDto create(UserDto user) {
        return UserDtoMapper.mapToUserDto(storage.create(UserDtoMapper.mapToUser(user)));
    }

    public UserDto update(UpdateUserDto newUser) {
        if (!storage.findAll().stream().map(User::getId).toList().contains(newUser.getId())) {
            throw new EntityNotFoundException("Пользователя с Id %d не существует".formatted(newUser.getId()));
        }
        User updatingUser = UserDtoMapper.mapToUser(getUser(newUser.getId()));
        if (newUser.hasEmail()) {
            updatingUser.setEmail(newUser.getEmail());
        }
        if (newUser.hasName()) {
            updatingUser.setName(newUser.getName());
        }
        if (newUser.hasLogin()) {
            updatingUser.setLogin(newUser.getLogin());
        }
        if (newUser.hasBirthdate()) {
            updatingUser.setBirthday(newUser.getBirthday());
        }
        return UserDtoMapper.mapToUserDto(storage.update(updatingUser));
    }

    public Collection<UserDto> findAll() {
        return storage.findAll().stream().map(UserDtoMapper::mapToUserDto).toList();
    }

    public UserDto getUser(Long id) {
        return UserDtoMapper.mapToUserDto(storage.getUser(id));
    }

    public Collection<UserDto> getMutualFriends(Long id, Long otherId) {
        if (storage.getUser(id) == null) {
            throw new EntityNotFoundException("Пользователя с id %d не существует".formatted(id));
        }
        if (storage.getUser(otherId) == null) {
            throw new EntityNotFoundException("Пользователя с otherId %d не существует".formatted(otherId));
        }
        return getUsers(storage.getMutualFriends(id, otherId));
    }

    public Collection<UserDto> getUsers(List<Long> friends) {
        return friends.stream().map(storage::getUser).map(UserDtoMapper::mapToUserDto).toList();
    }

}
