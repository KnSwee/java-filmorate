package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    FilmController films = new FilmController();

    private static Film.FilmBuilder getValidFilm() {
        return Film.builder()
                .id(1)
                .name("название")
                .description("описание")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(Duration.ofMinutes(90));
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectName() {
        Film blankFilm = getValidFilm().name("").build();
        Film nullFilm = getValidFilm().name(null).build();

        assertThrows(ValidationException.class, () -> films.create(blankFilm));
        assertThrows(ValidationException.class, () -> films.create(nullFilm));
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectDescription() {
        Film overlengthFilm = getValidFilm().description("BgtqyLpAxAcVAPFuclcUvsDpZuiETNHYwDMcNoAajZWVLkJSCIlRFYWR" +
                "kNvWgxUVAQtYgHkJrNyyLDSBwAvxtkauoQMKowpHKOvSwwSCXndqmUzEhUIEoEvwnxJflxOrZOBgpSLHKDkzoVZmceELKUum" +
                "ojdCRXiGoEvyDwUDXLdJDUcqwgqBtmRiunZSZdhoUACDMnMUJ").build();
        Film twoHundredlengthFilm = getValidFilm().description("mBgrxRNoqcHNEFbNBgRlUtJPBYZIsAJjJmXmprqroBkfhFwRBrF" +
                "ForRWHSaaNuaWYbRbQiyevcJSXObcoonDyTQCVccHjfStqypkwaObYIgwgOCwepEwbFujbRvmnGjRvktvATBTtWADbiCGDcqpZ" +
                "JVVBGfrfHCgWwHUvBFMQljToyJGFeftlIksvGUHiTZzxyvOGHJc").build();

        assertThrows(ValidationException.class, () -> films.create(overlengthFilm));
        assertEquals(twoHundredlengthFilm, films.create(twoHundredlengthFilm));
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectReleaseDate() {
        Film incorrectFilm = getValidFilm().releaseDate(LocalDate.of(1894, 1, 1)).build();
        Film correctFilm = getValidFilm().releaseDate(LocalDate.of(2077, 1, 1)).build();

        assertThrows(ValidationException.class, () -> films.create(incorrectFilm));
        assertEquals(correctFilm, films.create(correctFilm));
    }

    @Test
    public void shouldNotCreateFilmWithIncorrectDuration() {
        Film negativeDurationFilm = getValidFilm().duration(Duration.ofMinutes(-1)).build();
        Film zeroDurationFilm = getValidFilm().duration(Duration.ofMinutes(0)).build();
        Film correctFilm = getValidFilm().duration(Duration.ofMinutes(1)).build();

        assertThrows(ValidationException.class, () -> films.create(negativeDurationFilm));
        assertThrows(ValidationException.class, () -> films.create(zeroDurationFilm));
        assertEquals(correctFilm, films.create(correctFilm));
    }

    @Test
    public void shouldUpdateNullFilm() {
        Film film = films.create(getValidFilm().build());

        Film nullFilm = films.update(getValidFilm().name(null).description(null).releaseDate(null).duration(null).build());

        assertEquals(film, nullFilm);
    }


}
