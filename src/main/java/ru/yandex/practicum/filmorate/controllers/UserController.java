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

    private static final String RESPONSE_GET_TEXT = "Тело ответа на запрос GET: {}";

    @Autowired
    public UserController(ValidateService validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> allUsers() {
        log.info("GET запрос на получение всех пользователей получен.");
        List<User> users = userService.findAll();
        log.info(RESPONSE_GET_TEXT, users);
        return users;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("POST запрос на добавление пользователя получен. Тело запроса: {}", user);
        validator.validateUser(user);
        log.info("Валидация пройдена.");
        return userService.add(user);
    }

    @PutMapping
    public User updateuser(@Valid @RequestBody User user) {
        log.info("PUT запрос на обновление пользователя получен. Тело запроса: {}", user);
        validator.validateUser(user);
        log.info("Валидация пройдена.");
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("PUT запрос на добавление друзей получен. Тело запроса: user1 - {}, user2 - {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("DELETE запрос на удаление друзей получен. Тело запроса: user1 - {}, user2 - {}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info("GET запрос на получение друзей получен");
        List<User> friends = userService.getFriends(userService.getUser(id));
        log.info(RESPONSE_GET_TEXT, friends);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@PathVariable long id, @PathVariable long otherId) {
        log.info("GET запрос на получение общих друзей получен");
        List<User> friends = userService.getCommonFriends(id, otherId);
        log.info(RESPONSE_GET_TEXT, friends);
        return friends;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info("GET запрос на получение пользователя получен");
        User user = userService.getUser(id);
        log.info(RESPONSE_GET_TEXT, user);
        return user;
    }
}
