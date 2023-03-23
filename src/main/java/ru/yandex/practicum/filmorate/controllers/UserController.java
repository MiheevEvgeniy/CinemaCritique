package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RequestMapping("users")
@RestController
@Slf4j
public class UserController {
    private final ValidateService validator;
    private final UserService userService;

    @Autowired
    public UserController(ValidateService validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> allUsers() {
        log.info("Запрос GET получен.");
        log.info("Тело ответа на запрос GET: {}", userService.findAll());
        return userService.findAll();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Запрос POST получен.");
        log.info("Тело входящего запроса: {}", user);
        validator.validateUser(user);
        log.info("Валидация пройдена.");
        return userService.add(user);
    }

    @PutMapping
    public User updateuser(@Valid @RequestBody User user) {
        log.info("Запрос PUT получен.");
        log.info("Тело входящего запроса: {}", user);
        validator.validateUser(user);
        log.info("Валидация пройдена.");
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.error("" + userService.getUser(id));
        log.error("" + userService.getUser(friendId));
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return userService.getFriends(userService.getUser(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id);
    }
}
