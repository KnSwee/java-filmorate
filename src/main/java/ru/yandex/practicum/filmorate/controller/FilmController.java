package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Error;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@Validated
@RequestMapping("/films")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film, BindingResult result) {
        log.debug("Валидация добавления фильма");
        film.setId(getNextId());
        log.debug("Фильму присвоен id {}", film.getId());
        films.put(film.getId(), film);
        log.debug("Фильм добавлен со следующими параметрами - {}", films.get(film.getId()));
        System.out.println(result);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        if (oldFilm == null) {
            throw new ValidationException("Фильм с id " + newFilm.getId() + " не найден");
        }
        log.debug("Проверка введенных параметров фильма на null");
        filmUpdateNullChecker(newFilm, oldFilm);
        log.debug("Валидация обновления фильма");
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.debug("Информация о фильме обновлена. Новые параметры фильма - {}", films.get(oldFilm.getId()));
        return oldFilm;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void filmUpdateNullChecker(Film newFilm, Film oldFilm) {
        if (newFilm.getName() == null) {
            log.debug("При обновлении фильма не было введено название. Поле не обновляется, прежнее значение - {}",
                    oldFilm.getName());
            newFilm.setName(oldFilm.getName());
        }
        if (newFilm.getDescription() == null) {
            log.debug("При обновлении фильма не было введено описание. Поле не обновляется, прежнее значение - {}",
                    oldFilm.getDescription());
            newFilm.setDescription(oldFilm.getDescription());
        }
        if (newFilm.getReleaseDate() == null) {
            log.debug("При обновлении фильма не была введена дата релиза. Поле не обновляется, прежнее значение - {}",
                    oldFilm.getReleaseDate());
            newFilm.setReleaseDate(oldFilm.getReleaseDate());
        }
        if (newFilm.getDuration() == null) {
            log.debug("При обновлении фильма не была введена продолжительность фильма." +
                    " Поле не обновляется, прежнее значение - {}", formatDuration(oldFilm.getDuration()));
            newFilm.setDuration(oldFilm.getDuration());
        }
    }

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Error> handleValidationException(Throwable e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
