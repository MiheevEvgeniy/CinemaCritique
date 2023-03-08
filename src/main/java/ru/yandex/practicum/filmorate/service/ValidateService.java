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
    public boolean validateFilm(Film film) {
        if (film.getDescription().length() > 200) {
            log.error("max limit of description is 200 symbols. Got instead: {}", film.getDescription().length());
            throw new ValidationException();
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Invalid date. Date must to be after 28'th December 1895. Got instead: {}", film.getReleaseDate());
            throw new ValidationException();
        }
        if (film.getDuration() < 0.0) {
            log.error("Film's duration can't be negative number.");
            throw new ValidationException();
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Invalid film name: {}", film.getName());
            throw new ValidationException();
        }
        return true;
    }

    public boolean validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            log.error("Invalid email: {}", user.getEmail());
            throw new ValidationException();
        }
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.error("Invalid login: {}", user.getLogin());
            throw new ValidationException();
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Birthday can't be in the future");
            throw new ValidationException();
        }
        return true;
    }
}
