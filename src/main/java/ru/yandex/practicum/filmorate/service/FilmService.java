package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.models.Film;

public class FilmService {
    public Film addLike(long userId, Film film) {
        film.setLikes(film.getLikes() + 1);
        film.getUsersLiked().add(userId);
        return film;
    }

    public Film deleteLike(long userId, Film film) {
        if (film.getLikes() > 0) {
            film.setLikes(film.getLikes() - 1);
            film.getUsersLiked().remove(userId);
            return film;
        } else {
            throw new NullPointerException();
        }
    }
}
