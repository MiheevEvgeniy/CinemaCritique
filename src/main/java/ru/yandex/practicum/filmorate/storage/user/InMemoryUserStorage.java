package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 0;
    @Getter
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public long generateId() {
        return ++id;
    }

    @Override
    public User add(User user) {
        user.setId(generateId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("User added.");
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("User not found.");
            throw new UpdateException();
        }
        users.put(user.getId(), user);
        log.info("User updated.");
        return user;
    }

    @Override
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

    @Override
    public List<User> getCommonFriends(User user1, User user2) {
        if (user1.getFriends() == null || user2.getFriends() == null) {
            return Collections.emptyList();
        }
        List<Long> commonIds = new ArrayList<>(user1.getFriends());
        commonIds.retainAll(user2.getFriends());

        List<User> commonUser = new ArrayList<>();
        for (long id : commonIds) {
            commonUser.add(getUser(id));
        }
        return commonUser;
    }

    @Override
    public User getUser(long id) {
        if (users.get(id) == null) {
            throw new NullPointerException();
        } else {
            return users.get(id);
        }
    }
}
