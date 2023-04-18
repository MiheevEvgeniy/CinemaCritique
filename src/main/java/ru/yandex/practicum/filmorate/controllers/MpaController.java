package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.models.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;
import java.util.Optional;

@RequestMapping("mpa")
@RestController
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    public Optional<MPA> getMpa(@PathVariable long id) {
        return mpaService.getMpa(id);
    }

    @GetMapping
    public List<MPA> getAllMpa() {
        return mpaService.getAllMpa();
    }
}
