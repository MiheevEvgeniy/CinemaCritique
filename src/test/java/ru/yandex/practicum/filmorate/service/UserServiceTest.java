package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {
    private final UserService userService = new UserService(new InMemoryUserStorage());

    @Test
    public void addingAndDeletingFriends() {
        User user1 = User.builder()
                .id(1)
                .name("person1")
                .login("login1")
                .email("email1@mail.com")
                .birthday(LocalDate.MIN)
                .build();
        User user2 = User.builder()
                .id(2)
                .name("person2")
                .login("login2")
                .email("email2@mail.com")
                .birthday(LocalDate.MIN)
                .build();
        userService.add(user1);
        userService.add(user2);
        assertEquals(Collections.emptySet(), user1.getFriends());
        assertEquals(Collections.emptySet(), user2.getFriends());
        userService.addFriend(user1.getId(), user2.getId());
        assertEquals(Set.of(2L), user1.getFriends());
        assertEquals(Set.of(1L), user2.getFriends());
        userService.deleteFriend(user1.getId(), user2.getId());
        assertEquals(Collections.emptySet(), user1.getFriends());
        assertEquals(Collections.emptySet(), user2.getFriends());
        User user3 = User.builder()
                .id(3)
                .name("person3")
                .login("login3")
                .email("email3@mail.com")
                .birthday(LocalDate.MIN)
                .build();
        userService.add(user3);
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user2.getId(), user3.getId());
        assertEquals(List.of(user3), userService.getCommonFriends(user1.getId(), user2.getId()));
        List<User> users = userService.findAll();
        assertEquals(List.of(user1, user2, user3), users);
        User user4 = User.builder()
                .id(2)
                .name("person4")
                .login("login4")
                .email("email4@mail.com")
                .birthday(LocalDate.MIN)
                .build();
        userService.update(user4);
        users = userService.findAll();
        assertEquals(List.of(user1, user4, user3), users);
    }
}
