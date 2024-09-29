package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.dto.mapper.FilmDtoMapper;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmDbStorage storage;
    private final UserService userService;
    private final GenreService genreService;
    private final MpaService mpaService;

    public Long addLike(Long filmId, Long userId) {
        if (userService.getUser(userId) == null) {
            throw new EntityNotFoundException("Пользователя с id " + userId + " не существует");
        }
        if (storage.getFilm(filmId) == null) {
            throw new EntityNotFoundException("Фильма с id " + filmId + " не существует");
        }
        storage.addLike(filmId, userId);
        return userId;
    }

    public Long deleteLike(Long filmId, Long userId) {
        if (userService.getUser(userId) == null) {
            throw new EntityNotFoundException("Пользователя с id " + userId + " не существует");
        }
        if (storage.getFilm(filmId) == null) {
            throw new EntityNotFoundException("Фильма с id " + filmId + " не существует");
        }
        storage.deleteLike(filmId, userId);
        return userId;
    }

    public Collection<FilmDto> getRatedFilms(int count) {
        return storage.getRatedFilms(count).stream()
                .peek(film -> film.setMpa(mpaService.getMpaByFilm(film.getId())))
                .peek(film -> film.setLikes(storage.getLikes(film.getId())))
                .peek(film -> film.setGenres(genreService.getGenre(film.getId())))
                .map(FilmDtoMapper::mapToFilmDto)
                .toList();
    }

    private static int compare(FilmDto film1, FilmDto film2) {
        if (film1.getLikes().size() > film2.getLikes().size()) {
            return -1;
        } else if (film1.getLikes().size() < film2.getLikes().size()) {
            return 1;
        }
        return 0;
    }

    public FilmDto createFilm(FilmDto film) {
        Long newFilmId = storage.create(FilmDtoMapper.mapToFilm(film));
        mpaService.addMpa(film.getMpa(), newFilmId);
        if (!genreService.isGenreExist(film.getGenres())) {
            throw new IncorrectDataException("Не существует одного из представленных жанров - %s"
                    .formatted(film.getGenres()));
        }
        genreService.insertGenresToFilm(newFilmId, film.getGenres());

        return getFilm(newFilmId);
    }

    public FilmDto updateFilm(UpdateFilmDto film) {
        Film oldFilm = storage.getFilm(film.getId());
        if (film.getGenres() != null) {
            genreService.deleteGenres(film.getId());
            genreService.updateGenres(FilmDtoMapper.mapToFilm(film));
        }
        if (film.hasName()) {
            oldFilm.setName(film.getName());
        }
        if (film.hasDescription()) {
            oldFilm.setDescription(film.getDescription());
        }
        if (film.hasDuration()) {
            oldFilm.setDuration(film.getDuration());
        }
        if (film.hasReleaseDate()) {
            oldFilm.setReleaseDate(film.getReleaseDate());
        }
        if (film.getMpa() != null) {
            mpaService.addMpa(film.getMpa(), film.getId());
        }
        storage.update(oldFilm);
        return getFilm(film.getId());
    }

    public Collection<FilmDto> findAll() {
        return storage.findAll().stream()
                .peek(film -> film.setMpa(mpaService.getMpaByFilm(film.getId())))
                .peek(film -> film.setLikes(storage.getLikes(film.getId())))
                .peek(film -> film.setGenres(genreService.getGenre(film.getId())))
                .map(FilmDtoMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto getFilm(Long id) {
        Film film = storage.getFilm(id);
        film.setMpa(mpaService.getMpaByFilm(id));
        film.setLikes(storage.getLikes(id));
        film.setGenres(genreService.getGenre(id));
        return FilmDtoMapper.mapToFilmDto(film);
    }

}
