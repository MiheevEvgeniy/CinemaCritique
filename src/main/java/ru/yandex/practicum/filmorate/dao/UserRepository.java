package ru.yandex.practicum.filmorate.dao;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserRepository {
    private int id = 0;
    @Getter
    private final Map<Long, User> users = new HashMap<>();

    public long generateId() {
        return ++id;
    }

    public User add(User user) {
        user.setId(generateId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("User added.");
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("User not found.");
            throw new RuntimeException();
        }
        users.put(user.getId(), user);
        return user;
    }
}
