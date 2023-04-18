package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.models.MPA;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    Optional<MPA> getMpa(long id);

    List<MPA> getAllMpa();
}
