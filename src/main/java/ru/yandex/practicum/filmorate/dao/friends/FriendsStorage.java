package ru.yandex.practicum.filmorate.dao.friends;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface FriendsStorage {
    List<User> getFriendsByUserId(long id);

    void add(long userId, long friendId);

    void delete(long userId, long friendId);

    List<User> getCommonFriends(long id, long friendId);
}
