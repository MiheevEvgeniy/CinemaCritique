package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RequestMapping("films")
@RestController
@Slf4j
public class FilmController {
    private final ValidateService validator;
    private final FilmService filmService;

    private static final String RESPONSE_GET_TEXT = "Тело ответа на запрос GET: {}";

    @Autowired
    public FilmController(ValidateService validator, FilmService filmService) {
        this.validator = validator;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("GET запрос на получение всех фильмов получен.");
        List<Film> films = filmService.findAll();
        log.info(RESPONSE_GET_TEXT, films);
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("POST запрос на добавление фильма получен. Тело запроса: {}", film);
        validator.validateFilm(film);
        log.info("Валидация пройдена.");
        return filmService.add(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT запрос на обновление фильма получен. Тело запроса: {}", film);
        validator.validateFilm(film);
        log.info("Валидация пройдена.");
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public int addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT запрос на добавление лайка получен. Тело запроса: film - {}, user - {}", id, userId);
        filmService.addLike(userId, id);
        return 0;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public int deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE запрос на удаление лайка получен. Тело запроса: film - {}, user - {}", id, userId);
        filmService.deleteLike(userId, id);
        return 0;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("GET запрос на получение популярных фильмов получен. Тело запроса: count - {}", count);
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        log.info("GET запрос на получние фильма получен");
        Film film = filmService.getFilm(id);
        log.info(RESPONSE_GET_TEXT, film);
        return film;
    }
}
