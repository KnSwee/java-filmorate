package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage storage;
    private final UserService userService;

    public Long addLike(Long userId, Long filmId) {
        if (userService.getUser(userId) == null) {
            throw new EntityNotFoundException("Пользователя с id " + userId + " не существует");
        }
        if (storage.getFilm(filmId) == null) {
            throw new EntityNotFoundException("Фильма с id " + filmId + " не существует");
        }
        storage.getFilm(filmId).getLikes().add(userId);
        return userId;
    }

    public Long deleteLike(Long userId, Long filmId) {
        if (userService.getUser(userId) == null) {
            throw new EntityNotFoundException("Пользователя с id " + userId + " не существует");
        }
        if (storage.getFilm(filmId) == null) {
            throw new EntityNotFoundException("Фильма с id " + filmId + " не существует");
        }
        storage.getFilm(filmId).getLikes().remove(userId);
        return userId;
    }

    public Collection<Film> getRatedFilms(int count) {
        List<Film> ratedFilms = storage.findAll()
                .stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .toList()
                .reversed();

        return ratedFilms.subList(0, Math.min(count, ratedFilms.size()));
    }

    public Film createFilm(Film film) {
        return storage.create(film);
    }

    public Film updateFilm(Film film) {
        if (storage.getFilm(film.getId()) == null) {
            throw new EntityNotFoundException("Фильма с id " + film.getId() + " не существует");
        }
        return storage.update(film);
    }

    public Collection<Film> findAll() {
        return storage.findAll();
    }

    public Film getFilm(Long id) {
        return Optional.ofNullable(storage.getFilm(id))
                .orElseThrow(() -> new EntityNotFoundException("Фильма с id %d не существует".formatted(id)));
    }
}
