package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.mapper.MpaExistMapper;
import ru.yandex.practicum.filmorate.storage.mpa.mapper.MpaMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbc;
    private final MpaMapper mapper;
    private final MpaExistMapper existMapper;
    private static final String GET_RATING_QUERY = "SELECT * FROM rating;";
    private static final String GET_MPA_QUERY = "SELECT * FROM rating WHERE rating_id = ?;";
    private static final String ADD_RATING_QUERY = "UPDATE films SET rating_id = ? WHERE id = ?;";
    private static final String GET_RATING_BY_FILM_QUERY = "SELECT r.RATING_ID, r.NAME FROM RATING r RIGHT JOIN FILMS f ON r.RATING_ID = f.RATING_ID WHERE f.ID = ?;";
    private static final String UPDATE_RATING_QUERY = "UPDATE rating SET name = ? WHERE rating_id = ?;";
    private static final String DELETE_RATING_QUERY = "DELETE FROM rating WHERE rating_id = ?;";
    private static final String CHECK_RATING_EXIST_NAME = "SELECT COUNT(*) AS exist FROM rating WHERE name = ?;";
    private static final String CHECK_MPA_EXIST_ID = "SELECT COUNT(*) AS exist FROM rating WHERE rating_id = ?;";

    public Mpa getMpa(Long filmId) {
        return jdbc.query(GET_RATING_BY_FILM_QUERY, mapper, filmId).getFirst().getFirst();
    }

    public Mpa getMpaById(Long mpaId) {
        return jdbc.query(GET_MPA_QUERY, mapper, mpaId).getFirst().getFirst();
    }

    public boolean addMpa(Mpa mpa, Long filmId) {
        return jdbc.update(ADD_RATING_QUERY, mpa.getId(), filmId) > 0;
    }

    public Collection<Mpa> findAll() {
        return jdbc.query(GET_RATING_QUERY, mapper).getFirst();
    }

    public Long create(Mpa mpa) {
        Map<String, Object> ratingMap = new HashMap<>();
        ratingMap.put("name", mpa.getName());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbc)
                .withTableName("rating")
                .usingGeneratedKeyColumns("rating_id");

        return simpleJdbcInsert.executeAndReturnKey(ratingMap).longValue();
    }

    public Mpa update(Mpa mpa) {
        jdbc.update(UPDATE_RATING_QUERY, mpa.getName(), mpa.getId());
        return getMpa(mpa.getId());
    }

    public boolean delete(Long id) {
        return jdbc.update(DELETE_RATING_QUERY, id) > 0;
    }

    public boolean isMpaExist(String name) {
        return !jdbc.query(CHECK_RATING_EXIST_NAME, existMapper, name).isEmpty();
    }

    public boolean isMpaExistId(Long id) {
        return jdbc.query(CHECK_MPA_EXIST_ID, existMapper, id).getFirst();
    }
}
