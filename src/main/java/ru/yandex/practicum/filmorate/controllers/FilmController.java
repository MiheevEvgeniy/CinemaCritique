package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@RequestMapping("films")
@RestController
@Slf4j
public class FilmController {
    private final ValidateService validator = new ValidateService();
    private final FilmService filmService = new FilmService();
    private final InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("Запрос GET получен.");
        log.info("Тело ответа на запрос GET: {}", inMemoryFilmStorage.getFilms().values());
        return inMemoryFilmStorage.getFilms().values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Запрос POST получен.");
        log.info("Тело входящего запроса: {}", film);
        validator.validateFilm(film);
        log.info("Валидация пройдена.");
        return inMemoryFilmStorage.add(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос PUT получен.");
        log.info("Тело входящего запроса: {}", film);
        validator.validateFilm(film);
        log.info("Валидация пройдена.");
        return inMemoryFilmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        return inMemoryFilmStorage.update(filmService.addLike(userId, inMemoryFilmStorage.getFilm(id)));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        return inMemoryFilmStorage.update(filmService.deleteLike(userId, inMemoryFilmStorage.getFilm(id)));
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return inMemoryFilmStorage.getPopularFilms(new ArrayList<>(inMemoryFilmStorage.getFilms().values()), count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        e.printStackTrace();
        return Map.of("error", "Validation exception" + Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(final NullPointerException e) {
        e.printStackTrace();
        return Map.of("error", "Object not found" + Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalError(final RuntimeException e) {
        e.printStackTrace();
        return Map.of("error", "Unexpected error" + Arrays.toString(e.getStackTrace()));
    }
}
