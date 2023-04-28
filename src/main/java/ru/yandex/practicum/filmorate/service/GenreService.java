package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenresStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenresStorage storage;

    public GenreService(@Autowired @Qualifier("genresDbStorage") GenresStorage storage) {
        this.storage = storage;
    }

    public Genre getGenre(long id) {
        if (storage.getGenre(id).isPresent()) {
            return storage.getGenre(id).get();
        }
        throw new NotFoundDataException();
    }

    public List<Genre> getAllGenres() {
        return storage.getAllGenres();
    }
}
