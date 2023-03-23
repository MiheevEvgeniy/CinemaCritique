package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

interface UserStorage {
    long generateId();

    User add(User user);

    User update(User user);

    User getUser(long id);

    List<User> findAll();
}
