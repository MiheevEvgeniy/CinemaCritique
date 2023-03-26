package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmServiceTest {
    private UserService userService;
    private FilmService filmService;
    Film film1;
    Film film2;
    Film film3;
    User user1;

    @BeforeEach
    public void updateTestData() {
        userService = new UserService(new InMemoryUserStorage());
        filmService = new FilmService(new InMemoryFilmStorage());
        film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("description1")
                .rate(4)
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        film2 = Film.builder()
                .id(2)
                .name("film2")
                .description("description2")
                .rate(4)
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        user1 = User.builder()
                .id(1)
                .name("person1")
                .login("login1")
                .email("email1@mail.com")
                .birthday(LocalDate.MIN)
                .build();
        film3 = Film.builder()
                .id(1)
                .name("film3")
                .description("description3")
                .rate(4)
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
    }

    @Test
    public void addingFilm() {
        filmService.add(film1);
        filmService.add(film2);
        List<Film> films = filmService.findAll();
        assertEquals(List.of(film1, film2), films);
    }

    @Test
    public void updatingFilm() {
        filmService.add(film1);
        filmService.add(film2);
        List<Film> films = filmService.findAll();
        assertEquals(List.of(film1, film2), films);

        filmService.update(film3);
        films = filmService.findAll();
        assertEquals(List.of(film3, film2), films);
    }

    @Test
    public void addingAndDeletingLikesAndGettingPopularFilms() {
        filmService.add(film1);
        filmService.add(film2);
        userService.add(user1);
        assertEquals(0, film1.getLikes());
        assertEquals(0, film2.getLikes());
        filmService.addLike(1, 2);
        assertEquals(0, film1.getLikes());
        assertEquals(1, film2.getLikes());
        List<Film> popularFilms = filmService.getPopularFilms(2);
        assertEquals(film2, popularFilms.get(0));
        assertEquals(film1, popularFilms.get(1));
        filmService.deleteLike(1, 2);
        assertEquals(0, film1.getLikes());
        assertEquals(0, film2.getLikes());
    }
}
