package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.mapper.GenreExistMapper;
import ru.yandex.practicum.filmorate.storage.genre.mapper.GenresMapper;

import java.util.*;

@Component
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbc;
    private final GenresMapper mapper;
    private final GenreExistMapper existMapper;
    private static final String GET_GENRES_QUERY = "SELECT * FROM genre;";
    private static final String GET_GENRES_BY_FILM_QUERY = "SELECT g.GENRE_ID, g.NAME FROM GENRE g RIGHT JOIN FILM_GENRE fg ON g.GENRE_ID = fg.GENRE_ID WHERE fg.FILM_ID = ?;";
    private static final String UPDATE_GENRE_QUERY = "UPDATE genre SET name = ? WHERE genre_id = ?;";
    private static final String GET_GENRE_BY_ID = "SELECT * FROM genre WHERE genre_id = ?;";
    private static final String DELETE_GENRE_QUERY = "DELETE FROM genre WHERE genre_id = ?;";
    private static final String CHECK_GENRE_EXIST_NAME_QUERY = "SELECT COUNT(*) AS genre_exist FROM genre WHERE name = ?;";
    private static final String INSERT_GENRES_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";
    private static final String DELETE_FILM_GENRE_QUERY = "DELETE FROM film_genre WHERE film_id = ?;";
    private static final String CHECK_GENRE_EXIST_ID_QUERY = "SELECT COUNT(*) AS genre_exist FROM genre WHERE genre_id = ?;";


    public Set<Genre> getGenresByFilm(Long filmId) {
        List<List<Genre>> query = jdbc.query(GET_GENRES_BY_FILM_QUERY, mapper, filmId);
        if (query.isEmpty()) {
            return new HashSet<>();
        }
        return new LinkedHashSet<>(query.getFirst());
    }

    public Genre getGenreById(Long genreId) {
        List<List<Genre>> query = jdbc.query(GET_GENRE_BY_ID, mapper, genreId);
        if (query.isEmpty()) {
            return null;
        }
        return query.getFirst().getFirst();
    }

    public Collection<Genre> findAll() {
        return jdbc.query(GET_GENRES_QUERY, mapper).getFirst();
    }

    public Long create(Genre genre) {
        Map<String, Object> genreMap = new HashMap<>();
        genreMap.put("name", genre.getName());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc)
                .withTableName("genre")
                .usingGeneratedKeyColumns("genre_id");

        return simpleJdbcInsert.executeAndReturnKey(genreMap).longValue();
    }

    public boolean delete(Long id) {
        return jdbc.update(DELETE_GENRE_QUERY, id) > 0;
    }

    public boolean isGenreExistByName(String name) {
        return jdbc.query(CHECK_GENRE_EXIST_NAME_QUERY, existMapper, name).getFirst();
    }

    public void insertFilmGenres(Long filmId, Set<Genre> genres) {
        genres.forEach(genre -> jdbc.update(INSERT_GENRES_QUERY, filmId, genre.getId()));
    }

    public void updateGenres(Film updFilm) {
        Long id = updFilm.getId();
        deleteFilmGenres(id);
        updFilm.getGenres().forEach(genre -> jdbc.update(INSERT_GENRES_QUERY, id, genre.getId()));
    }

    public void deleteFilmGenres(Long filmId) {
        jdbc.update(DELETE_FILM_GENRE_QUERY, filmId);
    }

    public boolean isGenreExistId(Long genreId) {
        return jdbc.query(CHECK_GENRE_EXIST_ID_QUERY, existMapper, genreId).getFirst();
    }

}
