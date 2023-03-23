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

    @Autowired
    public FilmController(ValidateService validator, FilmService filmService) {
        this.validator = validator;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("Запрос GET получен.");
        log.info("Тело ответа на запрос GET: {}", filmService.findAll());
        return filmService.findAll();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Запрос POST получен.");
        log.info("Тело входящего запроса: {}", film);
        validator.validateFilm(film);
        log.info("Валидация пройдена.");
        return filmService.add(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос PUT получен.");
        log.info("Тело входящего запроса: {}", film);
        validator.validateFilm(film);
        log.info("Валидация пройдена.");
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.update(filmService.addLike(userId, id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.update(filmService.deleteLike(userId, id));
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmService.getFilm(id);
    }
}
