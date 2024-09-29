package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.dto.mapper.GenreDtoMapper;
import ru.yandex.practicum.filmorate.exception.DuplicateEntityException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;


    public Set<Genre> getGenre(Long filmId) {
        return genreDbStorage.getGenresByFilm(filmId);
    }

    public GenreDto getGenreById(Long genreId) {
        if (!genreDbStorage.isGenreExistId(genreId)) {
            throw new EntityNotFoundException("Жанра с id %s не существует".formatted(genreId));
        }
        return GenreDtoMapper.mapToGenreDto(genreDbStorage.getGenreById(genreId));
    }

    public Collection<GenreDto> findAll() {
        return genreDbStorage.findAll().stream().map(GenreDtoMapper::mapToGenreDto).toList();
    }

    public Long create(GenreDto newGenre) {
        if (genreDbStorage.isGenreExistByName(newGenre.getName())) {
            throw new DuplicateEntityException("Жанр %s уже существует".formatted(newGenre.getName()));
        }
        return genreDbStorage.create(GenreDtoMapper.mapToGenre(newGenre));
    }

    public boolean delete(Long genreId) {
        return genreDbStorage.delete(genreId);
    }

    public void insertGenresToFilm(Long id, List<GenreDto> genres) {
        genreDbStorage.insertFilmGenres(id, GenreDtoMapper.mapSetToGenre(genres));
    }

    public void deleteGenres(Long filmId) {
        genreDbStorage.deleteFilmGenres(filmId);
    }

    public void updateGenres(Film updFilm) {
        genreDbStorage.updateGenres(updFilm);
    }

    public boolean isGenreExist(List<GenreDto> genres) {
        return genres.stream().allMatch(genre -> genreDbStorage.isGenreExistId(genre.getId()));
    }


}
