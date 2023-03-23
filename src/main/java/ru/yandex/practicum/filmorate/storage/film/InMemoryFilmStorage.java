package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    @Getter
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
    public Film getFilm(long id) {
        if (films.get(id) == null) {
            throw new NullPointerException();
        } else {
            return films.get(id);
        }
    }

    public List<Film> getPopularFilms(List<Film> films, int count) {
        films.sort(Comparator.comparingLong(Film::getLikes));
        Collections.reverse(films);
        List<Film> topFilms = new ArrayList<>();

        if (count > films.size()) {
            count = films.size();
        }
        for (int i = 0; i < count; i++) {
            topFilms.add(films.get(i));
        }
        return topFilms;
    }
}
