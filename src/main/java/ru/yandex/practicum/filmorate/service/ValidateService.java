package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;

@Slf4j
@Component
public class ValidateService {
    public void validateFilm(Film film) {
        if (film.getDescription().length() > 200) {
            log.error("max limit of description is 200 symbols. Got instead: {}", film.getDescription().length());
            throw new ValidationException("Invalid description length");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Invalid date. Date must to be after 28'th December 1895. Got instead: {}", film.getReleaseDate());
            throw new ValidationException("Invalid release date");
        }
        if (film.getDuration() < 0.0) {
            log.warn("Film's duration can't be negative number.");
            throw new ValidationException("Invalid duration");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Invalid film name: {}", film.getName());
            throw new ValidationException("Invalid name");
        }
    }

    public void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            log.warn("Invalid email: {}", user.getEmail());
            throw new ValidationException("Invalid email");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.warn("Invalid login: {}", user.getLogin());
            throw new ValidationException("Invalid login");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Birthday can't be in the future");
            throw new ValidationException("Invalid birthday");
        }
    }
}
