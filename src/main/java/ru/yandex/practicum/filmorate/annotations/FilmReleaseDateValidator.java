package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FilmReleaseDateValidator
        implements ConstraintValidator<FilmReleaseDate, LocalDate> {
    LocalDate dateToCompare;
    boolean isAfter;

    public final void initialize(final FilmReleaseDate annotation) {
        this.dateToCompare = LocalDate.parse(annotation.value(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        this.isAfter = annotation.isAfter();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        return isAfter ? localDate.isAfter(dateToCompare) : localDate.isBefore(dateToCompare);
    }
}
