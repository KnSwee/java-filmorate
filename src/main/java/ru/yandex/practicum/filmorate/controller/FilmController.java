package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public FilmDto create(@Valid @RequestBody FilmDto film) {
        log.info("Запрос на создание фильма film {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public FilmDto update(@RequestBody UpdateFilmDto newFilm) {
        log.info("Запрос на обновление фильма с id {}, на film {}", newFilm.getId(), newFilm);
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Long addLike(@PathVariable("id") Long filmId,
                        @PathVariable Long userId) {
        log.info("Запрос на добавление лайка фильму с id {}, пользователем с id {}", filmId, userId);
        return filmService.addLike(filmId, userId);
    }

    @GetMapping
    public Collection<FilmDto> findAll() {
        log.info("Запрос на вывод всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public FilmDto getFilm(@PathVariable Long id) {
        log.info("Запрос на вывод фильма с id {}", id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getRatedFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос на получение популярных фильмов в количестве count {}", count);
        return filmService.getRatedFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Long deleteLike(@PathVariable("id") Long filmId,
                           @PathVariable Long userId) {
        log.info("Запрос на удаление лайка у фильма с {}, id пользователем с id {}", filmId, userId);
        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("{id}/genres")
    public Collection<GenreDto> getGenresById(@PathVariable("id") Long filmId) {
        log.info("Запрос на получение жанров фильма с id {}", filmId);
        return filmService.getFilm(filmId).getGenres();
    }

    @GetMapping("/{id}/mpa")
    public MpaDto getRatingByFilm(@PathVariable("id") Long filmId) {
        log.info("Запрос на получение рейтинга фильма с id {}", filmId);
        return filmService.getFilm(filmId).getMpa();
    }
}
