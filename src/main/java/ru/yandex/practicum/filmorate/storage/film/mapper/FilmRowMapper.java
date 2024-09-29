package ru.yandex.practicum.filmorate.storage.film.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmRowMapper implements RowMapper<List<Film>> {

    @Override
    public List<Film> mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Film> list = new ArrayList<>();
        do {
            Film iterFilm = new Film();
            iterFilm.setId(rs.getLong("id"));
            iterFilm.setName(rs.getString("name"));
            iterFilm.setDescription(rs.getString("description"));
            iterFilm.setReleaseDate(rs.getDate("release_date").toLocalDate());
            iterFilm.setDuration(Duration.ofSeconds(rs.getLong("duration_minutes")));
            list.add(iterFilm);
        } while (rs.next());
        return list;
    }
}

