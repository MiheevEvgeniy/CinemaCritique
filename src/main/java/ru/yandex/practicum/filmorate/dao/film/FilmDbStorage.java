package ru.yandex.practicum.filmorate.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        String sqlQuery = "INSERT INTO films (" +
                "description," +
                "name," +
                "release_date," +
                "duration," +
                "rate," +
                "likes," +
                "mpa_id)" +
                "VALUES (?,?,?,?,?,?,?);";
        long mpaId = 1;
        if (film.getMpa() != null) {
            mpaId = film.getMpa().getId();
        }
        jdbcTemplate.update(sqlQuery,
                film.getDescription(),
                film.getName(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getLikes(),
                mpaId);
        SqlRowSet filmsMaxId = jdbcTemplate.queryForRowSet("select MAX(film_id) AS film_id from films");
        Long filmId = null;
        if (filmsMaxId.next()) {
            filmId = filmsMaxId.getLong("film_id");
            film.setId(filmId);
        }

        if (film.getGenres() != null) {
            String friendsRows = "INSERT INTO film_genres (" +
                    "film_id," +
                    "genre_id)" +
                    "VALUES (?,?);";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(friendsRows, filmId, genre.getId());
            }
        }

        log.info("Фильм добавлен");
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!getFilm(film.getId()).isPresent()) {
            throw new UpdateException("Ошибка обновления фильма");
        }
        String sqlQuery = "UPDATE films SET " +
                "description=?," +
                "name=?," +
                "release_date=?," +
                "duration=?," +
                "rate=?," +
                "likes=?," +
                "mpa_id=? " +
                "WHERE film_id=?;";
        long mpaId = 1;
        if (film.getMpa() != null) {
            mpaId = film.getMpa().getId();
        }
        jdbcTemplate.update(sqlQuery,
                film.getDescription(),
                film.getName(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getLikes(),
                mpaId,
                film.getId()
        );

        if (film.getGenres() != null) {
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
        if (!film.getUsersLiked().isEmpty()) {
            String likesRows = "INSERT INTO film_liked_users (" +
                    "film_id," +
                    "user_id) " +
                    "VALUES (?,?) " +
                    "ON CONFLICT DO NOTHING";
            Long userId = new ArrayList<>(film.getUsersLiked()).get(film.getUsersLiked().size() - 1);
            jdbcTemplate.update(likesRows, film.getId(), userId);
        }
        if (film.getGenres() != null) {
            Set<Genre> uniqueGenres = new LinkedHashSet<>(film.getGenres());
            film.setGenres(new ArrayList<>(uniqueGenres));
        }

        log.info("Фильм обновлен");
        return film;
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query("select * from films", this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Long filmId = resultSet.getLong("film_id");
        String description = resultSet.getString("description");
        String name = resultSet.getString("name");
        LocalDate releaseDate = null;
        if (resultSet.getDate("release_date") != null) {
            releaseDate = Objects.requireNonNull(resultSet.getDate("release_date")).toLocalDate();
        }
        int rate = resultSet.getInt("rate");
        long duration = resultSet.getLong("duration");

        Set<Genre> genres = new HashSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres " +
                "WHERE genre_id IN (" +
                "SELECT genre_id FROM film_genres WHERE film_id = ?)", filmId);
        long genreId;
        String genreName;
        while (genreRows.next()) {
            genreId = genreRows.getLong("genre_id");
            genreName = genreRows.getString("name");
            genres.add(new Genre(genreId, genreName));
        }

        int likes = resultSet.getInt("likes");
        MPA mpa = MPA.builder().build();
        String sqlQuery = "SELECT name " +
                "FROM mpa " +
                "WHERE mpa_id=?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlQuery, resultSet.getInt("mpa_id"));
        if (mpaRow.next()) {
            mpa = MPA.builder().id(resultSet.getInt("mpa_id")).name(mpaRow.getString("name")).build();
        }
        return Film.builder()
                .id(filmId)
                .description(description)
                .name(name)
                .releaseDate(releaseDate)
                .rate(rate)
                .likes(likes)
                .duration(duration)
                .genres(new ArrayList<>(genres))
                .mpa(mpa)
                .build();
    }

    @Override
    public Optional<Film> getFilm(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where film_id = ?", id);

        if (filmRows.next()) {
            Long filmId = filmRows.getLong("film_id");
            String description = filmRows.getString("description");
            String name = filmRows.getString("name");
            long duration = filmRows.getLong("duration");
            LocalDate releaseDate = null;
            if (filmRows.getDate("release_date") != null) {
                releaseDate = Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate();
            }
            int likes = filmRows.getInt("likes");

            Set<Genre> genres = new HashSet<>();
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres " +
                    "WHERE genre_id IN (" +
                    "SELECT genre_id FROM film_genres WHERE film_id = ?)", id);
            long genreId;
            String genreName;
            while (genreRows.next()) {
                genreId = genreRows.getLong("genre_id");
                genreName = genreRows.getString("name");
                genres.add(new Genre(genreId, genreName));
            }

            int rate = filmRows.getInt("rate");
            MPA mpa = MPA.builder().build();
            String sqlQuery = "SELECT name " +
                    "FROM mpa " +
                    "WHERE mpa_id=?";
            SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sqlQuery, filmRows.getInt("mpa_id"));
            if (mpaRow.next()) {
                mpa = MPA.builder().id(filmRows.getInt("mpa_id")).name(mpaRow.getString("name")).build();
            }
            Film film = Film.builder()
                    .id(filmId)
                    .description(description)
                    .name(name)
                    .releaseDate(releaseDate)
                    .duration(duration)
                    .rate(rate)
                    .likes(likes)
                    .mpa(mpa)
                    .genres(new ArrayList<>(genres))
                    .build();
            log.info("Фильм с идентификатором {} найден.", id);
            return Optional.of(film);
        }
        log.info("Фильм с идентификатором {} не найден.", id);
        return Optional.empty();
    }
}
