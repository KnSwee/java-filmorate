package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateEntityException;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.film.mapper.LikeCheckMapper;
import ru.yandex.practicum.filmorate.storage.film.mapper.LikeMapper;

import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbc;
    private final FilmRowMapper mapper;
    private final LikeMapper likeMapper;
    private final LikeCheckMapper likeCheckMapper;
    private static final String ENTITY_NOT_FOUND = "Фильм с id %s не найден";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration_minutes = ? WHERE id = ?;";
    private static final String ADD_LIKE_QUERY = "INSERT INTO FILM_LIKE(FILM_ID, USER_ID) VALUES (?, ?);";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM FILM_LIKE WHERE film_id = ? AND user_id = ?;";
    private static final String LIKE_CHECK_QUERY = "SELECT COUNT(*) AS likes FROM film_like WHERE film_id = ? AND user_id = ?";
    private static final String GET_FILM_QUERY = "SELECT * FROM FILMS f WHERE id = ?;";
    private static final String FIND_ALL_QUERY = "SELECT * FROM FILMS f;";
    private static final String GET_LIKES_QUERY = "SELECT user_id FROM film_like fl WHERE film_id = ?;";
    private static final String GET_RATED_FILMS_QUERY = "SELECT count(DISTINCT fl.USER_ID) AS rate, f.ID, f.NAME, \n" +
            "f.DESCRIPTION, f.RELEASE_DATE, f.DURATION_MINUTES, f.RATING_ID\n" +
            "FROM FILMS f\n" +
            "LEFT JOIN FILM_LIKE fl ON f.ID = fl.FILM_ID\n" +
            "GROUP BY F.ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION_MINUTES, F.RATING_ID\n" +
            "ORDER BY rate DESC\n" +
            "LIMIT ?;";

    @Override
    public Long create(Film film) {

        Map<String, Object> filmMap = new HashMap<>();
        filmMap.put("name", film.getName());
        filmMap.put("description", film.getDescription());
        filmMap.put("release_date", java.sql.Date.valueOf(film.getReleaseDate()));
        filmMap.put("duration_minutes", film.getDuration());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        return simpleJdbcInsert.executeAndReturnKey(filmMap).longValue();
    }

    @Override
    public Film update(Film updFilm) {
        int rowsUpdated = jdbc.update(UPDATE_FILM_QUERY, updFilm.getName(), updFilm.getDescription(),
                updFilm.getReleaseDate(), updFilm.getDuration(), updFilm.getId());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return getFilm(updFilm.getId());
    }

    @Override
    public Collection<Film> findAll() {
        return Optional.of(jdbc.query(FIND_ALL_QUERY, mapper).getFirst())
                .orElseThrow(() -> new EntityNotFoundException("Не удалось получить фильмы"));
    }

    @Override
    public Film getFilm(Long filmId) {
        return Optional.ofNullable(jdbc.queryForObject(GET_FILM_QUERY, mapper, filmId).getFirst())
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.formatted(filmId)));
    }

    public Long addLike(Long filmId, Long userId) {
        if (likeCheck(filmId, userId)) {
            throw new DuplicateEntityException("Пользователь %s уже ставил фильму %s лайк".formatted(userId, filmId));
        }
        int likeAdded = jdbc.update(ADD_LIKE_QUERY, filmId, userId);
        if (likeAdded != 1) {
            throw new InternalServerException("Не удалось добавить лайк");
        }
        return filmId;
    }

    public Long deleteLike(Long filmId, Long userId) {
        int likeDeleted = jdbc.update(DELETE_LIKE_QUERY, filmId, userId);
        if (likeDeleted != 1) {
            throw new InternalServerException("Не удалось удалить лайк");
        }
        return filmId;
    }

    public Collection<Film> getRatedFilms(int count) {
        return Optional.of(jdbc.query(GET_RATED_FILMS_QUERY, mapper, count).getFirst())
                .orElseThrow(() -> new EntityNotFoundException("Не удалось получить топ фильмов"));
    }

    public boolean likeCheck(Long filmId, Long userId) {
        return jdbc.query(LIKE_CHECK_QUERY, likeCheckMapper, filmId, userId).getFirst();
    }

    public Set<Long> getLikes(Long filmId) {
        List<Set<Long>> query = jdbc.query(GET_LIKES_QUERY, likeMapper, filmId);
        if (query.isEmpty()) {
            return new HashSet<>();
        }
        return query.getFirst();
    }
}

