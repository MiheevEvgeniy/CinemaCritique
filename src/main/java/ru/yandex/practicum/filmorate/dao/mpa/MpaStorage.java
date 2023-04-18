package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    Optional<Mpa> getMpa(long id);

    List<Mpa> getAllMpa();
}
