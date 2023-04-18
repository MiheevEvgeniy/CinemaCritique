package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User add(User user);

    User update(User user);

    Optional<User> getUser(long id);

    List<User> findAll();
}
