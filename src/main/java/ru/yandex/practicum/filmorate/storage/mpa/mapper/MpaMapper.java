package ru.yandex.practicum.filmorate.storage.mpa.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MpaMapper implements RowMapper<List<Mpa>> {
    @Override
    public List<Mpa> mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Mpa> ratings = new ArrayList<>();
        do {
            ratings.add(new Mpa(rs.getLong("rating_id"), rs.getString("name")));
        } while (rs.next());
        return ratings;
    }
}
