package ru.yandex.practicum.filmorate.model;


import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Data
public class User {
    int id;
    //@Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    String email;
    //@NotNull
    String login;
    String name;
    //@PastOrPresent
    LocalDate birthday;

}
