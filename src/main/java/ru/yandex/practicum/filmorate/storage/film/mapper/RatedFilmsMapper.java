package ru.yandex.practicum.filmorate.storage.film.mapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RatedFilmsMapper implements RowMapper<List<Film>> {
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
            iterFilm.setMpa(new Mpa(rs.getLong("rating_id"), rs.getString("r_name")));

            String genresString = rs.getString("genres");
            if (!StringUtils.isBlank(genresString)) {
                Set<Genre> genres = Arrays.stream(genresString.split(","))
                        .map(genre -> genre.split("/"))
                        .map(parts -> new Genre(Long.parseLong(parts[0]), parts[1]))
                        .collect(Collectors.toCollection(LinkedHashSet::new));
                iterFilm.setGenres(genres);
            } else {
                iterFilm.setGenres(Set.of());
            }

            String likesString = rs.getString("likes");
            if (!StringUtils.isBlank(likesString)) {
                Set<Long> likes = Arrays.stream(likesString.split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toSet());
                iterFilm.setLikes(likes);
            } else {
                iterFilm.setLikes(Set.of());
            }

            list.add(iterFilm);
        } while (rs.next());
        return list;
    }
}
