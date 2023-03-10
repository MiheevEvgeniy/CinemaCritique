package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.Collection;

@RequestMapping("users")
@RestController
@Slf4j
public class UserController {
    private final ValidateService validator = new ValidateService();
    private final UserRepository userRepository = new UserRepository();

    @GetMapping
    public Collection<User> allUsers() {
        log.info("Запрос GET получен.");
        log.info("Тело ответа на запрос GET: {}", userRepository.getUsers().values());
        return userRepository.getUsers().values();
    }

    @PostMapping
    public User adduser(@Valid @RequestBody User user) {
        log.info("Запрос POST получен.");
        log.info("Тело входящего запроса: {}", user);
        validator.validateUser(user);
        log.info("Валидация пройдена.");
        return userRepository.add(user);
    }

    @PutMapping
    public User updateuser(@Valid @RequestBody User user) {
        log.info("Запрос PUT получен.");
        log.info("Тело входящего запроса: {}", user);
        validator.validateUser(user);
        log.info("Валидация пройдена.");
        return userRepository.update(user);
    }

}
