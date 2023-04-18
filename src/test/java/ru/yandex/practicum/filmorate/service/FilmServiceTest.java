package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.dao.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.dao.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceTest {
    private final FilmService filmService;

    private final UserService userService;
    private final LikesStorage likesStorage;

    Film film1;
    Film film2;
    Film film3;
    User user1;

    @BeforeEach
    public void updateTestData() {
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
        assertEquals(List.of(film1, film2), films);
    }

    @Test
    public void updatingFilm() {
        List<Film> films = filmService.findAll();
        assertEquals(List.of(film1, film2), films);

        filmService.update(film3);
        films = filmService.findAll();
        assertEquals(List.of(film3, film2), films);
    }

    @Test
    public void addingAndDeletingLikesAndGettingPopularFilms() {
        assertEquals(0, film1.getLikes());
        assertEquals(0, film2.getLikes());
        userService.add(user1);
        filmService.addLike(1, 2);
        assertEquals(0, likesStorage.getLikesByFilmId(film1.getId()));
        assertEquals(1, likesStorage.getLikesByFilmId(film2.getId()));
        List<Film> popularFilms = filmService.getPopularFilms(1);
        assertEquals(film2, popularFilms.get(0));
        filmService.deleteLike(1, 2);
        assertEquals(0, likesStorage.getLikesByFilmId(film1.getId()));
        assertEquals(0, likesStorage.getLikesByFilmId(film2.getId()));
    }
}
