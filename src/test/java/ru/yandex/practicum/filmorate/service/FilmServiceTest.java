package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmServiceTest {
    private final UserService userService = new UserService(new InMemoryUserStorage());
    private final FilmService filmService = new FilmService(new InMemoryFilmStorage());

    @Test
    public void addingAndDeletingLikes() {
        Film film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("description1")
                .rate(4)
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("film2")
                .description("description2")
                .rate(4)
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        filmService.add(film1);
        filmService.add(film2);
        User user1 = User.builder()
                .id(1)
                .name("person1")
                .login("login1")
                .email("email1@mail.com")
                .birthday(LocalDate.MIN)
                .build();
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
        List<Film> films = filmService.findAll();
        assertEquals(List.of(film1, film2), films);
        Film film3 = Film.builder()
                .id(1)
                .name("film3")
                .description("description3")
                .rate(4)
                .releaseDate(LocalDate.now())
                .duration(100)
                .build();
        filmService.update(film3);
        films = filmService.findAll();
        assertEquals(List.of(film3, film2), films);
    }
}
