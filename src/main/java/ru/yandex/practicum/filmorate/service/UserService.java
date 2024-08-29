package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final InMemoryUserStorage storage;

    public Long addFriend(Long userId, Long friendId) {
        if (storage.getUser(userId) == null) {
            throw new EntityNotFoundException("Пользователя с userId %d не существует".formatted(userId));
        }
        if (storage.getUser(friendId) == null) {
            throw new EntityNotFoundException("Пользователя с friendId %d не существует".formatted(friendId));
        }
        storage.getUser(userId).getFriends().add(friendId);
        storage.getUser(friendId).getFriends().add(userId);
        return friendId;
    }

    public Long deleteFriend(Long userId, Long friendId) {
        if (storage.getUser(userId) == null) {
            throw new EntityNotFoundException("Пользователя с userId %d не существует".formatted(userId));
        }
        if (storage.getUser(friendId) == null) {
            throw new EntityNotFoundException("Пользователя с friendId %d не существует".formatted(friendId));
        }
        storage.getUser(userId).getFriends().remove(friendId);
        storage.getUser(friendId).getFriends().remove(userId);
        return friendId;
    }

    public Collection<User> getFriends(Long userId) {
        if (storage.getUser(userId) == null) {
            throw new EntityNotFoundException("Пользователя с userId %d не существует".formatted(userId));
        }
        return storage.getUser(userId).getFriends()
                .stream()
                .map(storage::getUser)
                .toList();
    }

    public User create(User user) {
        return storage.create(user);
    }

    public User update(User newUser) {
        if (storage.getUser(newUser.getId()) == null) {
            throw new EntityNotFoundException("Пользователя с newUser.getId %d не существует".formatted(newUser.getId()));
        }
        return storage.update(newUser);
    }

    public Collection<User> findAll() {
        return storage.findAll();
    }

    public User getUser(Long id) {
        return Optional.ofNullable(storage.getUser(id))
                .orElseThrow(() -> new EntityNotFoundException("Пользователя с id %d не существует".formatted(id)));
    }

    public Collection<User> getMutualFriends(Long id, Long otherId) {
        if (storage.getUser(id) == null) {
            throw new EntityNotFoundException("Пользователя с id %d не существует".formatted(id));
        }
        if (storage.getUser(otherId) == null) {
            throw new EntityNotFoundException("Пользователя с otherId %d не существует".formatted(otherId));
        }

        ArrayList<User> mutualFriends = new ArrayList<>(getFriends(id));
        mutualFriends.retainAll(getFriends(otherId));
        return mutualFriends;
    }


}
