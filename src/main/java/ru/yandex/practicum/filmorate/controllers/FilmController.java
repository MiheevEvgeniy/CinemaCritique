package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.Collection;

@RequestMapping("films")
@RestController
@Slf4j
@Validated
public class FilmController {
    private final ValidateService validator = new ValidateService();

    private final FilmRepository filmRepository = new FilmRepository();

    @GetMapping
    public Collection<Film> allFilms() {
        return filmRepository.getFilms().values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        validator.validateFilm(film);
        return filmRepository.add(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validator.validateFilm(film);
        return filmRepository.update(film);
    }

}
