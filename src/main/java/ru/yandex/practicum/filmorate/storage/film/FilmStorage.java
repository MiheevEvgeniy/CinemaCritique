package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Film getFilm(long id);

    List<Film> findAll();
}
