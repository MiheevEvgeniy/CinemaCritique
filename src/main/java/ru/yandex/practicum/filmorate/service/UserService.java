package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserStorage storage;

    private final FriendsStorage friendsStorage;


    public UserService(@Autowired @Qualifier("userDbStorage") UserStorage userStorage,
                       @Autowired @Qualifier("friendsDbStorage") FriendsStorage friendsStorage) {
        this.storage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public void addFriend(long id, long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        if (user == null || friend == null) {
            throw new NotFoundDataException("Пользователь не найден. Дружба невозможна.");
        }
        friendsStorage.add(id, friendId);
        log.info("Пользователи теперь друзья");
    }

    public void deleteFriend(long id, long friendId) {
        User user = getUser(id);
        User friend = getUser(id);
        if (user == null || friend == null) {
            throw new NotFoundDataException("Пользователь не найден. Дружба невозможна.");
        }
        friendsStorage.delete(id, friendId);
        log.info("Пользователи больше не друзья");
    }

    public List<User> getCommonFriends(long id, long friendId) {
        return friendsStorage.getCommonFriends(id, friendId);
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

    public List<User> getFriends(long id) {
        return friendsStorage.getFriendsByUserId(id);
    }

    public User getUser(long id) {
        Optional<User> user = storage.getUser(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new NotFoundDataException();
    }

    public List<User> findAll() {
        return storage.findAll();
    }
}
