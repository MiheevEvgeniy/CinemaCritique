package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequestMapping("users")
@RestController
@Slf4j
public class UserController {
    private final ValidateService validator = new ValidateService();
    private final UserService userService = new UserService();
    private final InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @GetMapping
    public Collection<User> allUsers() {
        log.info("Запрос GET получен.");
        log.info("Тело ответа на запрос GET: {}", inMemoryUserStorage.getUsers().values());
        return inMemoryUserStorage.getUsers().values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Запрос POST получен.");
        log.info("Тело входящего запроса: {}", user);
        validator.validateUser(user);
        log.info("Валидация пройдена.");
        return inMemoryUserStorage.add(user);
    }

    @PutMapping
    public User updateuser(@Valid @RequestBody User user) {
        log.info("Запрос PUT получен.");
        log.info("Тело входящего запроса: {}", user);
        validator.validateUser(user);
        log.info("Валидация пройдена.");
        return inMemoryUserStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.error("" + inMemoryUserStorage.getUser(id));
        log.error("" + inMemoryUserStorage.getUser(friendId));
        userService.addFriend(inMemoryUserStorage.getUser(id), inMemoryUserStorage.getUser(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(inMemoryUserStorage.getUser(id), inMemoryUserStorage.getUser(friendId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return inMemoryUserStorage.getFriends(inMemoryUserStorage.getUser(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@PathVariable long id, @PathVariable long otherId) {
        return inMemoryUserStorage.getCommonFriends(inMemoryUserStorage.getUser(id), inMemoryUserStorage.getUser(otherId));
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return inMemoryUserStorage.getUser(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        e.printStackTrace();
        return Map.of("error", "Validation exception" + Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(final NullPointerException e) {
        e.printStackTrace();
        return Map.of("error", "Object not found" + Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalError(final RuntimeException e) {
        e.printStackTrace();
        return Map.of("error", "Unexpected error" + Arrays.toString(e.getStackTrace()));
    }
}
