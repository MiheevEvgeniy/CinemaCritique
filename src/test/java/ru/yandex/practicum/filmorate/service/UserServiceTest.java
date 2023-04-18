package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.dao.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {
    private UserService userService;
    User user1;
    User user2;
    User user3;
    User user4;

    @BeforeEach
    public void updateTestData() {

        userService = new UserService(new InMemoryUserStorage());
        user1 = User.builder()
                .id(1)
                .name("person1")
                .login("login1")
                .email("email1@mail.com")
                .birthday(LocalDate.MIN)
                .build();
        user2 = User.builder()
                .id(2)
                .name("person2")
                .login("login2")
                .email("email2@mail.com")
                .birthday(LocalDate.MIN)
                .build();
        user3 = User.builder()
                .id(3)
                .name("person3")
                .login("login3")
                .email("email3@mail.com")
                .birthday(LocalDate.MIN)
                .build();
        user4 = User.builder()
                .id(2)
                .name("person4")
                .login("login4")
                .email("email4@mail.com")
                .birthday(LocalDate.MIN)
                .build();
    }

    @Test
    public void addingUsers() {
        userService.add(user1);
        userService.add(user2);
        userService.add(user3);
        assertEquals(List.of(user1, user2, user3), userService.findAll());
    }

    @Test
    public void updatingUsers() {
        userService.add(user1);
        userService.add(user2);
        userService.add(user3);
        assertEquals(List.of(user1, user2, user3), userService.findAll());

        userService.update(user4);

        assertEquals(user4, userService.getUser(2));
        assertEquals(List.of(user1, user4, user3), userService.findAll());
    }

    @Test
    public void addingAndDeletingFriends() {
        userService.add(user1);
        userService.add(user2);
        assertEquals(Collections.emptyList(), user1.getFriends());
        assertEquals(Collections.emptyList(), user2.getFriends());

        userService.addFriend(user1.getId(), user2.getId());
        assertEquals(List.of(2L), user1.getFriends());

        userService.deleteFriend(user1.getId(), user2.getId());
        assertEquals(Collections.emptyList(), user1.getFriends());
        assertEquals(Collections.emptyList(), user2.getFriends());

        userService.add(user3);
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user2.getId(), user3.getId());
        assertEquals(List.of(user3), userService.getCommonFriends(user1.getId(), user2.getId()));
    }

}
