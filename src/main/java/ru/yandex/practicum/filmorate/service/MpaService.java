package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.models.MPA;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage storage;

    public MpaService(@Autowired @Qualifier("mpaDbStorage") MpaStorage storage) {
        this.storage = storage;
    }

    public Optional<MPA> getMpa(long id) {
        if (storage.getMpa(id).isPresent()) {
            return storage.getMpa(id);
        }
        throw new NotFoundDataException();

    }

    public List<MPA> getAllMpa() {
        return storage.getAllMpa();
    }
}
