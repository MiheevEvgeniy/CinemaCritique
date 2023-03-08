package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.Collection;

@RequestMapping("users")
@RestController
@Slf4j
@Validated
public class UserController {
    private final ValidateService validator = new ValidateService();
    private final UserRepository userRepository = new UserRepository();

    @GetMapping
    public Collection<User> allUsers() {
        return userRepository.getUsers().values();
    }

    @PostMapping
    public User adduser(@Valid @RequestBody User user) {
        validator.validateUser(user);
        return userRepository.add(user);
    }

    @PutMapping
    public User updateuser(@Valid @RequestBody User user) {
        validator.validateUser(user);
        return userRepository.update(user);
    }

}
