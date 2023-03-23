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

    @Override
    public long generateId() {
        return ++id;
    }

    @Override
    public Film add(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Film added.");
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Film not found.");
            throw new UpdateException();
        }
        films.put(film.getId(), film);
        log.info("Film updated.");
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
            throw new NullPointerException();
        }
        return film;
    }
}
