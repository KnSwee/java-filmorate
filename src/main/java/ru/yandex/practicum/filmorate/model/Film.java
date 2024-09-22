package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.annotations.FilmReleaseDate;
import ru.yandex.practicum.filmorate.annotations.PositiveDuration;
import ru.yandex.practicum.filmorate.config.serializers.DurationSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public class Film {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @FilmReleaseDate(value = "28.12.1895", message = "дата релиза должна быть не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    @PositiveDuration(message = "Продолжительность фильма должна быть положительным числом")
    private Duration duration;
    private Set<Long> likes = new HashSet<>();
}
