package ru.yandex.practicum.filmorate.storage.genre.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenresExistByFilmMapper implements RowMapper<List<Long>> {
    @Override
    public List<Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArrayList<Long> genres = new ArrayList<>();
        do {
            genres.add(rs.getLong("genre_id"));
        } while (rs.next());
        return genres;
    }
}
