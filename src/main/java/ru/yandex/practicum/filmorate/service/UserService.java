package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.storage = userStorage;
    }

    public void addFriend(long id, long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        log.info("Пользователи найдены");
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        log.info("Пользователи теперь друзья");
    }

    public void deleteFriend(long id, long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        log.info("Пользователи найдены");
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        log.info("Пользователи больше не друзья");
    }

    public List<User> getCommonFriends(long id, long friendId) {
        User user1 = getUser(id);
        User user2 = getUser(friendId);
        log.info("Пользователи получены");
        if (user1.getFriends() == null || user2.getFriends() == null) {
            log.info("У одного или обоих пользователей нет друзей. Возвращен пустой список общих друзей");
            return Collections.emptyList();
        }
        List<Long> commonIds = new ArrayList<>(user1.getFriends());
        commonIds.retainAll(user2.getFriends());
        log.info("Списки друзей сравнены и получены id общих друзей");
        List<User> commonUser = new ArrayList<>();
        for (long commonId : commonIds) {
            commonUser.add(storage.getUser(commonId));
        }
        log.info("По id друзей сформирован список общих друзей-объектов");
        return commonUser;
    }

    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("У пользователя отсутствует имя. Установлен логин как имя по умолчанию");
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
            log.info("У пользователя нет друзей");
            return Collections.emptyList();
        }
        for (long friendId : user.getFriends()) {
            friends.add(getUser(friendId));
        }
        log.info("Друзья получены");
        return friends;
    }

    public User getUser(long id) {
        return storage.getUser(id);
    }

    public List<User> findAll() {
        return storage.findAll();
    }
}
