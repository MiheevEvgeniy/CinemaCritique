package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.storage = userStorage;
    }

    public Set<Long> addFriend(long id1, long id2) {
        User user = getUser(id1);
        User friend = getUser(id2);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        return user.getFriends();
    }

    public Set<Long> deleteFriend(long id1, long id2) {
        User user = getUser(id1);
        User friend = getUser(id2);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        return user.getFriends();
    }

    public List<User> getCommonFriends(long id1, long id2) {
        User user1 = getUser(id1);
        User user2 = getUser(id2);
        if (user1.getFriends() == null || user2.getFriends() == null) {
            return Collections.emptyList();
        }
        List<Long> commonIds = new ArrayList<>(user1.getFriends());
        commonIds.retainAll(user2.getFriends());

        List<User> commonUser = new ArrayList<>();
        for (long id : commonIds) {
            commonUser.add(storage.getUser(id));
        }
        return commonUser;
    }

    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return storage.add(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public List<User> getFriends(User user) {
        List<User> friends = new ArrayList<>();
        if (user.getFriends() == null) {
            return Collections.emptyList();
        }
        for (long friendId : user.getFriends()) {
            friends.add(getUser(friendId));
        }
        return friends;
    }

    public User getUser(long id) {
        return storage.getUser(id);
    }

    public List<User> findAll() {
        return storage.findAll();
    }
}
