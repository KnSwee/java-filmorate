package ru.yandex.practicum.filmorate.storage.user.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MutualFriendsMapper implements RowMapper<List<Long>> {
    @Override
    public List<Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Long> mutualFriends = new ArrayList<>();
        do {
            mutualFriends.add(rs.getLong("mutual_friends"));
        } while (rs.next());
        return mutualFriends;
    }
}
