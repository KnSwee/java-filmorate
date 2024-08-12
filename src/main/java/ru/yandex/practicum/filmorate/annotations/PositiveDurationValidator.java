package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class PositiveDurationValidator
        implements ConstraintValidator<PositiveDuration, Duration> {

    public final void initialize(final PositiveDuration annotation) {
    }

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext context) {
        return duration.isPositive();
    }
}