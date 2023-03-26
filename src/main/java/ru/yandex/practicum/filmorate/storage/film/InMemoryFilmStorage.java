package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private final Map<Long, Film> films = new HashMap<>();

    public long generateId() {
        return ++id;
    }

    @Override
    public Film add(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм не найден");
            throw new UpdateException();
        }
        films.put(film.getId(), film);
        log.info("Фильм обновлен");
        return film;
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(long id) {
        Film film = films.get(id);
        if (film == null) {
            log.error("Фильм не найден");
            throw new NullPointerException("Фильм не найден");
        }
        log.info("Фильм найден");
        return film;
    }
}
