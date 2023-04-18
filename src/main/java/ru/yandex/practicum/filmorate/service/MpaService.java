package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage storage;

    public MpaService(@Autowired @Qualifier("mpaDbStorage") MpaStorage storage) {
        this.storage = storage;
    }

    public Mpa getMpa(long id) {
        if (storage.getMpa(id).isPresent()) {
            return storage.getMpa(id).get();
        }
        throw new NotFoundDataException();

    }

    public List<Mpa> getAllMpa() {
        return storage.getAllMpa();
    }
}
