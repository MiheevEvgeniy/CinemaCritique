package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserStorage {
    public long generateId();

    public User add(User user);

    public User update(User user);

    public User getUser(long id);

    public List<User> getFriends(User user);

    public List<User> getCommonFriends(User user1, User user2);
}
