package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    private final Map<Long, User> users = new HashMap<>();

    public long generateId() {
        return ++id;
    }

    @Override
    public User add(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь не найден");
            throw new UpdateException("Ошибка обновления пользователя");
        }
        users.put(user.getId(), user);
        log.info("Пользователь обновлен");
        return user;
    }

    @Override
    public User getUser(long id) {
        if (users.get(id) == null) {
            log.error("Пользователь не найден");
            throw new NullPointerException("Пользователь не найден");
        }
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
