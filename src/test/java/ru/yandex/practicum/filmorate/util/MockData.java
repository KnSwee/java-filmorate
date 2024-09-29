package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

public class MockData {

    public static Film getBaseFilm() {
        return new Film(null, "newFilm", "newDescr",
                LocalDate.of(2001, 1, 1), Duration.ofMinutes(10),
                null, null, null);
    }

    public static User getBaseUser() {
        return new User(null, "example@example.com", "login", "name",
                LocalDate.of(2000, 2, 2), null);
    }

    public static Genre getBaseGenre() {
        return new Genre(null, "new");
    }

    public static Mpa getBaseMpa() {
        return new Mpa(null, "new");
    }

}
