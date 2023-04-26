package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    private final UserService userService;
    static User user1;
    static User user2;
    static User user3;
    static User user4;

    @BeforeAll
    public static void updateTestData() {

        user1 = User.builder()
                .id(1)
                .name("person1")
                .login("login1")
                .email("email1@mail.com")
                .birthday(LocalDate.now())
                .build();
        user2 = User.builder()
                .id(2)
                .name("person2")
                .login("login2")
                .email("email2@mail.com")
                .birthday(LocalDate.now())
                .build();
        user3 = User.builder()
                .id(3)
                .name("person3")
                .login("login3")
                .email("email3@mail.com")
                .birthday(LocalDate.now())
                .build();
        user4 = User.builder()
                .id(2)
                .name("person4")
                .login("login4")
                .email("email4@mail.com")
                .birthday(LocalDate.now())
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
        assertEquals(List.of(user1, user2, user3), userService.findAll());

        userService.update(user4);

        assertEquals(user4, userService.getUser(2));
        assertEquals(List.of(user1, user4, user3), userService.findAll());
    }

    @Test
    public void addingAndDeletingFriends() {
        assertEquals(Collections.emptyList(), userService.getFriends(user1.getId()));
        assertEquals(Collections.emptyList(), userService.getFriends(user2.getId()));

        userService.addFriend(user1.getId(), user2.getId());
        assertEquals(2L, userService.getFriends(user1.getId()).get(0).getId());

        userService.deleteFriend(user1.getId(), user2.getId());
        assertEquals(Collections.emptyList(), userService.getFriends(user1.getId()));
        assertEquals(Collections.emptyList(), userService.getFriends(user2.getId()));

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user2.getId(), user3.getId());
        assertEquals(List.of(user3), userService.getCommonFriends(user1.getId(), user2.getId()));
    }

}
