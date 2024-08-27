package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmReleaseDateValidator.class)
@Documented
public @interface FilmReleaseDate {
    String message() default "{PeriodValidator.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value() default "";

    boolean isAfter() default true;
}
