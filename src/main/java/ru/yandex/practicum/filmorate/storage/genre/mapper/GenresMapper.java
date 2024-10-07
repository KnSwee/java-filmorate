package ru.yandex.practicum.filmorate.storage.genre.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenresMapper implements RowMapper<List<Genre>> {
    @Override
    public List<Genre> mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        do {
            genres.add(new Genre(rs.getLong("genre_id"), rs.getString("name")));
        } while (rs.next());
        return genres;
    }
}
