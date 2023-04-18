package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage storage;

    public GenreService(@Autowired @Qualifier("genreDbStorage") GenreStorage storage) {
        this.storage = storage;
    }

    public Optional<Genre> getGenre(long id) {
        if (storage.getGenre(id).isPresent()) {
            return storage.getGenre(id);
        }
        throw new NotFoundDataException();
    }

    public List<Genre> getAllGenres() {
        return storage.getAllGenres();
    }
}
