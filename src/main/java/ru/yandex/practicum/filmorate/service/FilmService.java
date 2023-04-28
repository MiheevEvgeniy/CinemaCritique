package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.genre.GenresStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage storage;
    private final LikesStorage likesStorage;
    private final GenresStorage genresStorage;
    private final UserStorage userStorage;

    public FilmService(@Autowired @Qualifier("filmDbStorage") FilmStorage storage,
                       @Autowired @Qualifier("likesDbStorage") LikesStorage likesStorage,
                       @Autowired @Qualifier("userDbStorage") UserStorage userStorage,
                       @Autowired @Qualifier("genresDbStorage") GenresStorage genresStorage) {
        this.storage = storage;
        this.likesStorage = likesStorage;
        this.userStorage = userStorage;
        this.genresStorage = genresStorage;
    }

    public void addLike(long userId, long id) {
        if (userStorage.getUser(userId).isEmpty()) {
            throw new NotFoundDataException("Пользователь не найден");
        }
        likesStorage.addLike(userId, id);
        log.info("Лайк добавлен");
    }

    public void deleteLike(long userId, long id) {
        if (userStorage.getUser(userId).isEmpty()) {
            throw new NotFoundDataException("Пользователь не найден");
        }
        likesStorage.deleteLike(userId, id);
        log.info("Лайк удален.");
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = likesStorage.getPopularFilms(count);
        for (Film film : films) {
            film.setGenres(genresStorage.getGenresByFilmId(film.getId()));
        }
        return films;
    }

    public Film add(Film film) {
        return storage.add(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public List<Film> findAll() {
        return storage.findAll();
    }

    public Film getFilm(long id) {
        if (storage.getFilm(id).isPresent()) {
            return storage.getFilm(id).get();
        }
        throw new NotFoundDataException();
    }
}
