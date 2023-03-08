package ru.yandex.practicum.filmorate.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class FilmRepository {
    private int id = 0;
    @Getter
    private final Map<Long, Film> films = new HashMap<>();

    public long generateId() {
        return ++id;
    }

    public Film add(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Film added.");
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Film not found.");
            throw new RuntimeException();
        }
        films.put(film.getId(), film);
        return film;
    }
}
