package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private long id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String login;
    private String name;
    @Builder.Default
    private LocalDate birthday = LocalDate.now();
}
