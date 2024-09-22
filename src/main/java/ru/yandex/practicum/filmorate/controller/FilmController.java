package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;


    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на создание фильма film {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Запрос на обновление фильма с id {}, на film {}", newFilm.getId(), newFilm);
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Long addLike(@PathVariable("id") Long filmId,
                        @PathVariable Long userId) {
        log.info("Запрос на добавление лайка фильму с id {}, пользователем с id {}", filmId, userId);
        return filmService.addLike(userId, filmId);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос на вывод всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.info("Запрос на вывод фильма с id {}", id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getRatedFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос на получение популярных фильмов в количестве count {}", count);
        return filmService.getRatedFilms(count);
    }


    @DeleteMapping("/{id}/like/{userId}")
    public Long deleteLike(@PathVariable("id") Long filmId,
                           @PathVariable Long userId) {
        log.info("Запрос на удаление лайка у фильма с {}, id пользователем с id {}", filmId, userId);
        return filmService.deleteLike(userId, filmId);
    }

}
