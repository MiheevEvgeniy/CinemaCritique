package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.Collection;

@RequestMapping("films")
@RestController
@Slf4j
public class FilmController {
    private final ValidateService validator = new ValidateService();

    private final FilmRepository filmRepository = new FilmRepository();

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("Запрос GET получен.");
        log.info("Тело ответа на запрос GET: {}", filmRepository.getFilms().values());
        return filmRepository.getFilms().values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Запрос POST получен.");
        log.info("Тело входящего запроса: {}", film);
        validator.validateFilm(film);
        log.info("Валидация пройдена.");
        return filmRepository.add(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Запрос PUT получен.");
        log.info("Тело входящего запроса: {}", film);
        validator.validateFilm(film);
        log.info("Валидация пройдена.");
        return filmRepository.update(film);
    }

}
