package ru.yandex.practicum.filmorate.storageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.util.MockData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
public class GenreStorageTest {
    private final GenreDbStorage storage;
    private final FilmDbStorage filmStorage;


    @Test
    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createGenreTest() {
        long genreId = storage.create(MockData.getBaseGenre());


        assertEquals(MockData.getBaseGenre().getName(), storage.getGenreById(genreId).getName());
    }

    @Test
    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void insertGenresTest() {
        long film = filmStorage.create(MockData.getBaseFilm());
        long genre1Id = storage.create(MockData.getBaseGenre());
        long genre2Id = storage.create(MockData.getBaseGenre());
        long genre3Id = storage.create(MockData.getBaseGenre());
        Set<Genre> genres = new HashSet<>(List.of(storage.getGenreById(genre1Id),
                storage.getGenreById(genre2Id), storage.getGenreById(genre3Id)));

        storage.insertFilmGenres(film, genres);

        Set<Genre> genresByFilm = storage.getGenresByFilm(film);
        assertEquals(3, genresByFilm.size());
        assertTrue(genresByFilm.contains(storage.getGenreById(genre1Id)));
        assertTrue(genresByFilm.contains(storage.getGenreById(genre2Id)));
        assertTrue(genresByFilm.contains(storage.getGenreById(genre3Id)));
    }

    @Test
    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void getGenreTest() {
        long film = filmStorage.create(MockData.getBaseFilm());
        long genreId = storage.create(MockData.getBaseGenre());
        storage.insertFilmGenres(film, new HashSet<>(List.of(storage.getGenreById(genreId))));

        assertEquals(MockData.getBaseGenre().getName(), storage.getGenreById(genreId).getName());
        assertEquals(MockData.getBaseGenre().getName(),
                storage.getGenresByFilm(film).stream().findFirst().orElse(null).getName());
    }

    @Test
    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateGenresTest() {
        Long filmId = filmStorage.create(MockData.getBaseFilm());
        long genre1Id = storage.create(MockData.getBaseGenre());
        long genre2Id = storage.create(MockData.getBaseGenre());
        long genre3Id = storage.create(MockData.getBaseGenre());
        Set<Genre> genres = new HashSet<>(List.of(storage.getGenreById(genre1Id),
                storage.getGenreById(genre2Id), storage.getGenreById(genre3Id)));
        Set<Genre> newGenres = new HashSet<>(List.of(storage.getGenreById(genre3Id), storage.getGenreById(genre1Id)));


        storage.insertFilmGenres(filmId, genres);
        Film updFilm = MockData.getBaseFilm();
        updFilm.setId(filmId);
        updFilm.setGenres(newGenres);

        storage.updateGenres(updFilm);

        Set<Genre> genresByFilm = storage.getGenresByFilm(filmId);
        assertEquals(2, genresByFilm.size());
        assertTrue(genresByFilm.contains(storage.getGenreById(genre3Id)));
        assertTrue(genresByFilm.contains(storage.getGenreById(genre1Id)));
    }

    @Test
    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllGenresTest() {
        long genre1Id = storage.create(MockData.getBaseGenre());
        long genre2Id = storage.create(MockData.getBaseGenre());
        long genre3Id = storage.create(MockData.getBaseGenre());

        assertEquals(3, storage.findAll().size());
        assertTrue(storage.findAll().contains(storage.getGenreById(genre1Id)));
        assertTrue(storage.findAll().contains(storage.getGenreById(genre2Id)));
        assertTrue(storage.findAll().contains(storage.getGenreById(genre3Id)));
    }

    @Test
    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteGenreTest() {
        long genre1Id = storage.create(MockData.getBaseGenre());
        long genre2Id = storage.create(MockData.getBaseGenre());
        Genre genre1 = storage.getGenreById(genre1Id);

        assertEquals(2, storage.findAll().size());

        storage.delete(genre1Id);

        assertEquals(1, storage.findAll().size());
        assertFalse(storage.findAll().contains(genre1));
    }

    @Test
    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void genreExistTest() {
        long genre1Id = storage.create(MockData.getBaseGenre());

        assertTrue(storage.isGenreExistByName("new"));
        assertTrue(storage.isGenreExistId(genre1Id));
    }

}
