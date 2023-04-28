package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenresStorage {
    Optional<Genre> getGenre(long id);

    List<Genre> getAllGenres();

    public List<Genre> getGenresByFilmId(long id);
}
