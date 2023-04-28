package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Optional<Film> getFilm(long id);

    List<Film> findAll();
}
