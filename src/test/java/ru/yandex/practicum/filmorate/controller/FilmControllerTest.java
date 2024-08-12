package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static Film.FilmBuilder getValidFilm() {
        return Film.builder()
                .id(1)
                .name("название")
                .description("описание")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(Duration.ofMinutes(90));
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectName() {
        Film blankFilm = getValidFilm().name("").build();
        Film nullFilm = getValidFilm().name(null).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(blankFilm);
        violations.addAll(validator.validate(nullFilm));

        assertEquals(2, violations.size());
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectDescription() {
        Film overlengthFilm = getValidFilm().description("a".repeat(201)).build();
        Film twoHundredlengthFilm = getValidFilm().description("a".repeat(200)).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(overlengthFilm);
        violations.addAll(validator.validate(twoHundredlengthFilm));

        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectReleaseDate() {
        Film incorrectFilm = getValidFilm().releaseDate(LocalDate.of(1894, 1, 1)).build();
        Film correctFilm = getValidFilm().releaseDate(LocalDate.of(2077, 1, 1)).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(incorrectFilm);
        violations.addAll(validator.validate(correctFilm));

        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectDuration() {
        Film negativeDurationFilm = getValidFilm().duration(Duration.ofMinutes(-1)).build();
        Film zeroDurationFilm = getValidFilm().duration(Duration.ofMinutes(0)).build();
        Film correctFilm = getValidFilm().duration(Duration.ofMinutes(1)).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(negativeDurationFilm);
        violations.addAll(validator.validate(zeroDurationFilm));
        violations.addAll(validator.validate(correctFilm));

        assertEquals(2, violations.size());
    }

}
