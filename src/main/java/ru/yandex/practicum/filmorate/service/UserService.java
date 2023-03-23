package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;

import java.util.Set;

@Service
public class UserService {
    public Set<Long> addFriend(User user, User friend) {
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        return user.getFriends();
    }

    public Set<Long> deleteFriend(User user, User friend) {
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        return user.getFriends();
    }
}
