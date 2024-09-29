package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public class Mpa {
    private Long id;
    private String name;
}
