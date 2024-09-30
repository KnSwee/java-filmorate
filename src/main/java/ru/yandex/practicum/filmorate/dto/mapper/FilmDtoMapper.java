package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class FilmDtoMapper {

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setLikes(film.getLikes());
        dto.setGenres(GenreDtoMapper.mapSetToDto(film.getGenres()));
        dto.setMpa(MpaDtoMapper.mapToMpaDto(film.getMpa()));
        return dto;
    }

    public static Film mapToFilm(FilmDto filmDto) {
        Film film = new Film();
        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());
        film.setLikes(filmDto.getLikes());
        film.setGenres(GenreDtoMapper.mapSetToGenre(filmDto.getGenres()));
        film.setMpa(MpaDtoMapper.mapToMpa(filmDto.getMpa()));
        return film;
    }

    public static Film mapToFilm(UpdateFilmDto filmDto) {
        Film film = new Film();
        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());
        film.setGenres(filmDto.getGenres().stream().map(GenreDtoMapper::mapToGenre).collect(Collectors.toSet()));
        film.setMpa(MpaDtoMapper.mapToMpa(filmDto.getMpa()));
        return film;
    }

}
