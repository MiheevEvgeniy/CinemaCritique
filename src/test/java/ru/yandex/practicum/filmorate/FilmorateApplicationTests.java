package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    public void testAddUser() {
        userStorage.add(User.builder().name("Test user").birthday(LocalDate.MIN).build());
        int usersCount = userStorage.findAll().size();
        Optional<User> userOptional = userStorage.getUser(usersCount);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Test user")
                );
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.getUser(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testUpdateUser() {
        userStorage.update(User.builder().id(1).name("Test user2").birthday(LocalDate.MIN).build());

        Optional<User> userOptional = userStorage.getUser(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Test user2")
                ).hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    public void testAddFilm() {
        filmStorage.add(Film.builder().name("Test film").mpa(Mpa.builder().build()).build());
        int filmsCount = filmStorage.findAll().size();
        Optional<Film> filmOptional = filmStorage.getFilm(filmsCount);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Test film")
                );
    }

    @Test
    public void testUpdateFilm() {
        filmStorage.update(Film.builder().id(1).name("Test film2").mpa(Mpa.builder().build()).build());

        Optional<Film> filmOptional = filmStorage.getFilm(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Test film2")
                ).hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = filmStorage.getFilm(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
}