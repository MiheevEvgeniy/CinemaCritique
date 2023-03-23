package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.models.Film;

public interface FilmStorage {
    public long generateId();

    public Film add(Film film);

    public Film update(Film film);

    public Film getFilm(long id);
}
