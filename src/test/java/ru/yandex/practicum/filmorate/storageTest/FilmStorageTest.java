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
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.util.MockData;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
public class FilmStorageTest {
    private final FilmDbStorage storage;
    private final UserDbStorage userDbStorage;

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void createAndGetFilmTest() {
        Film film = MockData.getBaseFilm();
        Long id = storage.create(film);
        film.setId(id);

        Film dbFilm = storage.getFilm(id);

        assertEquals(film, dbFilm);
        assertEquals(film.getName(), dbFilm.getName());
        assertEquals(film.getDescription(), dbFilm.getDescription());
        assertEquals(film.getReleaseDate(), dbFilm.getReleaseDate());
        assertEquals(film.getDuration(), dbFilm.getDuration());
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void updateFilmTest() {
        Film film = MockData.getBaseFilm();
        Long id = storage.create(film);
        film.setId(id);
        Film updFilm = getBaseFilm2(id);

        storage.update(updFilm);
        Film dbFilm = storage.getFilm(id);

        assertEquals(updFilm, dbFilm);
        assertEquals(updFilm.getName(), dbFilm.getName());
        assertEquals(updFilm.getDescription(), dbFilm.getDescription());
        assertEquals(updFilm.getReleaseDate(), dbFilm.getReleaseDate());
        assertEquals(updFilm.getDuration(), dbFilm.getDuration());
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void findAllTest() {
        Film film = MockData.getBaseFilm();
        getBaseFilm2(null);
        storage.create(film);
        storage.create(film);


    }

    @Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void addLikeTest() {
        Film film = MockData.getBaseFilm();
        Long id = storage.create(film);

        assertEquals(0, storage.getLikes(id).size());

        storage.addLike(id, 1L);

        assertEquals(1, storage.getLikes(id).size());

        storage.deleteLike(id, 1L);

        assertEquals(0, storage.getLikes(id).size());
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void getRatedFilmsTest() {
        Long film1 = storage.create(MockData.getBaseFilm());
        Long film2 = storage.create(MockData.getBaseFilm());
        Long topFilm = storage.create(MockData.getBaseFilm());

        Long user1Id = userDbStorage.create(MockData.getBaseUser()).getId();
        Long user2Id = userDbStorage.create(MockData.getBaseUser()).getId();
        Long user3Id = userDbStorage.create(MockData.getBaseUser()).getId();

        storage.addLike(film1, user1Id);
        storage.addLike(film2, user1Id);
        storage.addLike(film2, user2Id);
        storage.addLike(topFilm, user1Id);
        storage.addLike(topFilm, user2Id);
        storage.addLike(topFilm, user3Id);

        System.out.println(storage.getLikes(film1));
        System.out.println(storage.getLikes(film2));
        System.out.println(storage.getLikes(topFilm));

        Long dbTopFilm = storage.getRatedFilms(3).stream().findFirst().orElse(null).getId();
        assertEquals(3, storage.getLikes(dbTopFilm).size());
    }

    @Sql(value = {"/schema.sql", "/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void likeCheckTest() {
        Long film1 = storage.create(MockData.getBaseFilm());
        Long user1Id = userDbStorage.create(MockData.getBaseUser()).getId();

        storage.addLike(film1, user1Id);

        assertTrue(storage.likeCheck(film1, user1Id));
    }

    private Film getBaseFilm2(Long id) {
        return new Film(id, "newFilm2", "newDescr2",
                LocalDate.of(2002, 2, 2), Duration.ofMinutes(20),
                null, null, null);
    }

}
