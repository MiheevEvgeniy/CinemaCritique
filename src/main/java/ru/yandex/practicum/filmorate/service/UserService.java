package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage storage;


    public UserService(@Autowired @Qualifier("userDbStorage") UserStorage userStorage) {
        this.storage = userStorage;
    }

    public void addFriend(long id, long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        log.info("Пользователи найдены");
        user.getFriends().add(friend.getId());
        //friend.getFriends().put(user.getId(),FriendshipStatus.NOT_CONFIRMED);
        update(user);
        log.info("Пользователи теперь друзья");
    }

    public void deleteFriend(long id, long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        log.info("Пользователи найдены");
        user.getFriends().remove(friend.getId());
        //friend.getFriends().remove(user.getId());
        update(user);
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
        List<Long> commonIds = user1.getFriends();
        commonIds.retainAll(user2.getFriends());
        log.info("Списки друзей сравнены и получены id общих друзей");
        List<User> commonUser = new ArrayList<>();
        for (long commonId : commonIds) {
            if (storage.getUser(commonId).isPresent()) {
                if (storage.getUser(commonId).isPresent()) {
                    commonUser.add(storage.getUser(commonId).get());
                }
            }
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

    public List<User> getFriends(long id) {
        User user = getUser(id);
        System.out.println(user.getFriends());
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
        if (storage.getUser(id).isPresent()) {
            return storage.getUser(id).get();
        }
        throw new NotFoundDataException();
    }

    public List<User> findAll() {
        return storage.findAll();
    }
}
