package ru.yandex.practicum.filmorate.dto.film;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import ru.yandex.practicum.filmorate.annotations.FilmReleaseDate;
import ru.yandex.practicum.filmorate.annotations.PositiveDuration;
import ru.yandex.practicum.filmorate.config.serializers.DurationDeserializer;
import ru.yandex.practicum.filmorate.config.serializers.DurationSerializer;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class UpdateFilmDto {
    private Long id;
    private String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;
    @FilmReleaseDate(value = "28.12.1895", message = "дата релиза должна быть не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @JsonDeserialize(using = DurationDeserializer.class)
    @JsonSerialize(using = DurationSerializer.class)
    @PositiveDuration(message = "Продолжительность фильма должна быть положительным числом")
    private Duration duration;
    private Set<GenreDto> genres = new LinkedHashSet<>();
    private MpaDto mpa;

    public boolean hasName() {
        return StringUtils.isNotBlank(this.name);
    }

    public boolean hasDescription() {
        return StringUtils.isNotBlank(this.description);
    }

    public boolean hasReleaseDate() {
        return this.releaseDate != null;
    }

    public boolean hasDuration() {
        return this.duration != null;
    }

    public boolean hasMpa() {
        return this.mpa != null;
    }

}
