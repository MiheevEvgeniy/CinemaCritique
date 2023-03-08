package ru.yandex.practicum.filmorate.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

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
