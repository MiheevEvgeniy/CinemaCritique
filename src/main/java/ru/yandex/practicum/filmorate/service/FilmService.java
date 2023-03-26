package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage storage;

    @Autowired
    public FilmService(InMemoryFilmStorage storage) {
        this.storage = storage;
    }

    public Film addLike(long userId, long id) {
        Film film = storage.getFilm(id);
        film.setLikes(film.getLikes() + 1);
        film.getUsersLiked().add(userId);
        log.info("Лайк добавлен");
        return film;
    }

    public Film deleteLike(long userId, long id) {
        Film film = storage.getFilm(id);
        if (film.getLikes() > 0) {
            film.setLikes(film.getLikes() - 1);
            film.getUsersLiked().remove(userId);
            log.info("Лайк удален");
            return film;
        }
        log.error("Количество лайков <= 0. Удаление невозможно");
        return null;
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> films = storage.findAll();
        films.sort(Comparator.comparingLong(Film::getLikes));
        Collections.reverse(films);
        List<Film> topFilms = new ArrayList<>();
        log.info("Список фильмов отсортирован по лайкам");
        if (count > films.size()) {
            count = films.size();
        }
        for (int i = 0; i < count; i++) {
            topFilms.add(films.get(i));
        }
        log.info("Список из {} популярных фильмов получен", count);
        return topFilms;
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
        return storage.getFilm(id);
    }
}
