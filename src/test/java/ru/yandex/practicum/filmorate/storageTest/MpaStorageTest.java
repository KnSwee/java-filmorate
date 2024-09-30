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
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.util.MockData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
public class MpaStorageTest {
    private final MpaDbStorage storage;
    private final FilmDbStorage filmStorage;

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void createMpaTest() {
        Long mpaId = storage.create(MockData.getBaseMpa());

        assertEquals("new", storage.getMpaById(mpaId).getName());
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void getAndAddMpaTest() {
        Film film = MockData.getBaseFilm();
        Long filmId = filmStorage.create(film);
        Long mpaId = storage.create(MockData.getBaseMpa());
        storage.addMpa(storage.getMpaById(mpaId), filmId);

        assertEquals("new", storage.getMpa(filmId).getName());
        assertEquals("new", storage.getMpaById(mpaId).getName());
        assertEquals(mpaId, storage.getMpa(filmId).getId());
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void findAllMpaTest() {
        Long mpaId = storage.create(MockData.getBaseMpa());
        Long mpa2Id = storage.create(MockData.getBaseMpa());
        Long mpa3Id = storage.create(MockData.getBaseMpa());

        assertEquals(3, storage.findAll().size());
        assertTrue(storage.findAll().contains(storage.getMpaById(mpaId)));
        assertTrue(storage.findAll().contains(storage.getMpaById(mpa2Id)));
        assertTrue(storage.findAll().contains(storage.getMpaById(mpa3Id)));
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void deleteMpaTest() {
        Long mpaId = storage.create(MockData.getBaseMpa());
        Long mpa2Id = storage.create(MockData.getBaseMpa());
        Long mpa3Id = storage.create(MockData.getBaseMpa());

        assertEquals(3, storage.findAll().size());

        storage.delete(mpa2Id);

        assertEquals(2, storage.findAll().size());
        assertTrue(storage.findAll().contains(storage.getMpaById(mpaId)));
        assertTrue(storage.findAll().contains(storage.getMpaById(mpa3Id)));
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void existMpaTest() {
        Long mpaId = storage.create(MockData.getBaseMpa());

        assertTrue(storage.isMpaExistId(mpaId));
        assertTrue(storage.isMpaExist("new"));
    }

}
