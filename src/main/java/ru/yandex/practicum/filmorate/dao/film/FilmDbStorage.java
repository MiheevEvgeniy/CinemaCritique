package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.genre.GenresStorage;
import ru.yandex.practicum.filmorate.dao.likes.LikesStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private final GenresStorage genresStorage;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         @Qualifier("genresDbStorage") GenresStorage genresStorage,
                         @Qualifier("likesDbStorage") LikesStorage likesStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genresStorage = genresStorage;
        this.likesStorage = likesStorage;
    }

    @Override
    public Film add(Film film) {

        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, rate, mpa_id) " +
                "VALUES (?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());

        if (film.getGenres() != null) {
            saveGenres(film);
        }

        log.info("Фильм добавлен");
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film == null || getFilm(film.getId()).isEmpty()) {
            throw new NotFoundDataException("Фильм не найден");
        }
        String sqlQuery = "UPDATE films SET " +
                "description=?," +
                "name=?," +
                "release_date=?," +
                "duration=?," +
                "rate=?," +
                "mpa_id=? " +
                "WHERE film_id=?;";
        jdbcTemplate.update(sqlQuery,
                film.getDescription(),
                film.getName(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres() != null) {
            saveGenres(film);
            Set<Genre> uniqueGenres = new LinkedHashSet<>(film.getGenres());
            film.setGenres(new ArrayList<>(uniqueGenres));
        }
        log.info("Фильм обновлен");
        return film;
    }

    private void saveGenres(Film film) {
        String deleteFriendsRow = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteFriendsRow, film.getId());

        String genreRows = "INSERT INTO film_genres (" +
                "film_id," +
                "genre_id) " +
                "VALUES (?,?) " +
                "ON CONFLICT DO NOTHING";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(genreRows, film.getId(), genre.getId());
        }
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query("SELECT * FROM films f, MPA m " +
                "WHERE f.mpa_id = m.mpa_id", this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .description(resultSet.getString("description"))
                .name(resultSet.getString("name"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .rate(resultSet.getInt("rate"))
                .duration(resultSet.getLong("duration"))
                .mpa(Mpa.builder().id(resultSet.getInt("MPA.mpa_id")).name(resultSet.getString("MPA.name")).build())
                .genres(genresStorage.getGenresByFilmId(resultSet.getLong("film_id")))
                .build();
    }

    @Override
    public Optional<Film> getFilm(long id) {
        final String sqlQuery = "SELECT * FROM films f, MPA m " +
                "WHERE f.mpa_id = m.mpa_id AND film_id = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id);
        if (films.size() != 1) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundDataException("film id=" + id);
        }
        log.info("Фильм с идентификатором {} найден.", id);
        return Optional.of(films.get(0));
    }
}
