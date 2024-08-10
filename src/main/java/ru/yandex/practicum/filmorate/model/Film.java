package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import ru.yandex.practicum.filmorate.config.serializers.DurationSerializer;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public class Film {
    int id;
    //@NotNull
    //@NotBlank
    String name;
    //@Size(max = 200)
    String description;

    LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    //@PositiveOrZero
    Duration duration;
}
