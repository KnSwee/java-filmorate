package ru.yandex.practicum.filmorate.storage.user.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class FriendListMapper implements RowMapper<ArrayList<Long>> {
    @Override
    public ArrayList<Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
        ArrayList<Long> friends = new ArrayList<>();
        do {
            friends.add(rs.getLong("id"));
        } while (rs.next());
        return friends;
    }
}
