package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceTest {
    private final FilmService filmService;

    private final UserService userService;
    private final LikesStorage likesStorage;

    static Film film1;
    static Film film2;
    static Film film3;
    static User user1;

    @BeforeAll
    public static void updateTestData() {
        film1 = Film.builder()
                .id(1)
                .name("film1")
                .description("description1")
                .rate(4)
                .releaseDate(LocalDate.now())
                .duration(100)
                .mpa(Mpa.builder().name("G").build())
                .build();
        film2 = Film.builder()
                .id(2)
                .name("film2")
                .description("description2")
                .rate(4)
                .releaseDate(LocalDate.now())
                .duration(100)
                .mpa(Mpa.builder().name("G").build())
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
                .mpa(Mpa.builder().name("G").build())
                .duration(100)
                .build();
    }

    @Test
    public void addingFilm() {
        filmService.add(film1);
        filmService.add(film2);
        List<Film> films = filmService.findAll();
        assertTrue(films.size()>0);
    }

    @Test
    public void updatingFilm() {
        List<Film> films = filmService.findAll();
        assertTrue(films.size()>0);

        Film filmNotUpdated = filmService.getFilm(film3.getId());
        filmService.update(film3);

        assertTrue(films.size()>0);
        assertNotEquals(filmNotUpdated, filmService.getFilm(film3.getId()));

    }

    @Test
    public void addingAndDeletingLikesAndGettingPopularFilms() {
        assertEquals(0, film1.getLikes());
        userService.add(user1);
        filmService.addLike(1, 2);
        assertEquals(0, likesStorage.getLikesByFilmId(film1.getId()));
        assertEquals(1, likesStorage.getLikesByFilmId(film2.getId()));
        List<Film> popularFilms = filmService.getPopularFilms(1);
        assertEquals(film2.getName(), popularFilms.get(0).getName());
        filmService.deleteLike(1, 2);
        assertEquals(0, likesStorage.getLikesByFilmId(film1.getId()));
        assertEquals(0, likesStorage.getLikesByFilmId(film2.getId()));
    }
}
