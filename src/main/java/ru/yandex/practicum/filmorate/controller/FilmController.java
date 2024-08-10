package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate ACCEPTABLE_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.debug("Валидация добавления фильма");
        filmValidation(film);
        film.setId(getNextId());
        log.debug("Фильму присвоен id {}", film.getId());
        films.put(film.getId(), film);
        log.debug("Фильм добавлен со следующими параметрами - {}", films.get(film.getId()));
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        Film oldFilm = films.get(newFilm.getId());
        log.debug("Проверка введенных параметров фильма на null");
        filmUpdateNullChecker(newFilm, oldFilm);
        log.debug("Валидация обновления фильма");
        filmValidation(newFilm);
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

    private void filmValidation(Film film) {
        if (isBlank(film.getName())) {
            log.error("Было введено пустое название фильма");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Длина описания превышает допустимую. Длина введенного описания - {}. Допустимо - 200",
                    film.getDescription().length());
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(ACCEPTABLE_RELEASE_DATE)) {
            log.error("Введенная дата релиза раньше допустимой. Введенная дата - {}. Допустимая - {} и позже",
                    film.getReleaseDate().format(FORMATTER), ACCEPTABLE_RELEASE_DATE.format(FORMATTER));
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        if (!film.getDuration().isPositive()) {
            log.error("Введена отрицательная или нулевая продолжительность фильма - {}", formatDuration(film.getDuration()));
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
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

}
