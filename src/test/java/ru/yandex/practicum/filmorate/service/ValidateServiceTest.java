package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidateServiceTest {
    private final ValidateService validator = new ValidateService();

    @Test
    public void descriptionFilm() {
        StringBuilder stringBuilder = new StringBuilder();
        String longDescription;
        for (int i = 0; i < 200; i++) {
            stringBuilder.append('c');
        }
        longDescription = stringBuilder.toString();
        Film film = Film.builder()
                .name("film1")
                .description(longDescription)
                .releaseDate(LocalDate.now())
                .build();
        assertDoesNotThrow(() -> validator.validateFilm(film));
        film.setDescription(stringBuilder.append('c').toString());
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateFilm(film);
            }
        });
    }

    @Test
    public void releaseDateFilm() {
        Film film = Film.builder()
                .name("film1")
                .description("description1")
                .build();
        film.setReleaseDate(LocalDate.MAX);
        assertDoesNotThrow(() -> validator.validateFilm(film));
        film.setReleaseDate(LocalDate.now());
        assertDoesNotThrow(() -> validator.validateFilm(film));
        film.setReleaseDate(LocalDate.MIN);
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateFilm(film);
            }
        });
    }

    @Test
    public void nameFilm() {
        Film film = Film.builder()
                .description("description1")
                .build();
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateFilm(film);
            }
        });
        film.setName("");
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateFilm(film);
            }
        });
        film.setName("name");
        assertDoesNotThrow(() -> validator.validateFilm(film));
    }

    @Test
    public void durationFilm() {
        Film film = Film.builder()
                .name("film1")
                .description("description1")
                .releaseDate(LocalDate.now())
                .build();
        assertDoesNotThrow(() -> validator.validateFilm(film));
        film.setDuration(1.0);
        assertDoesNotThrow(() -> validator.validateFilm(film));
        film.setDuration(-1.0);
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateFilm(film);
            }
        });
    }

    @Test
    public void emailUser() {
        User user = User.builder()
                .name("user1")
                .login("myself123")
                .build();
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateUser(user);
            }
        });
        user.setEmail("my_email@gmail.com");
        assertDoesNotThrow(() -> validator.validateUser(user));
        user.setEmail("my_email.gmail.com");
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateUser(user);
            }
        });
    }

    @Test
    public void loginUser() {
        User user = User.builder()
                .name("user1")
                .email("my_email@gmail.com")
                .build();
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateUser(user);
            }
        });
        user.setLogin("myself123");
        assertDoesNotThrow(() -> validator.validateUser(user));
        user.setLogin("myself 123");
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateUser(user);
            }
        });
    }

    @Test
    public void birthdayUser() {
        User user = User.builder()
                .name("user1")
                .email("my_email@gmail.com")
                .login("myself123")
                .build();
        assertDoesNotThrow(() -> validator.validateUser(user));
        user.setBirthday(LocalDate.MIN);
        assertDoesNotThrow(() -> validator.validateUser(user));
        user.setBirthday(LocalDate.now());
        assertDoesNotThrow(() -> validator.validateUser(user));
        user.setBirthday(LocalDate.MAX);
        assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                validator.validateUser(user);
            }
        });
    }
}
