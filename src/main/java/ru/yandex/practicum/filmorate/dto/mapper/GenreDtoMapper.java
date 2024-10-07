package ru.yandex.practicum.filmorate.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class GenreDtoMapper {

    public static GenreDto mapToGenreDto(Genre genre) {
        GenreDto dto = new GenreDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }

    public static Genre mapToGenre(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setName(genreDto.getName());
        return genre;
    }

    public static List<GenreDto> mapSetToDto(Set<Genre> genres) {
        return genres.stream()
                .map(GenreDtoMapper::mapToGenreDto)
                .toList();
    }

    public static Set<Genre> mapSetToGenre(List<GenreDto> genres) {
        return genres.stream()
                .map(GenreDtoMapper::mapToGenre)
                .collect(Collector.of(LinkedHashSet::new, LinkedHashSet::add,
                        (set1, set2) -> {
                            set1.addAll(set2);
                            return set1;
                        }, Collector.Characteristics.IDENTITY_FINISH));
    }
}
