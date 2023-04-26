package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> getGenre(long id);

    List<Genre> getAllGenres();
}
