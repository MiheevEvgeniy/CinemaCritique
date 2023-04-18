package ru.yandex.practicum.filmorate.dao.likes;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface LikesStorage {
    Integer getLikesByFilmId(long filmId);

    void deleteLike(long userId, long filmId);

    void addLike(long userId, long filmId);

    List<Film> getPopularFilms(int count);
}
