package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    @NotNull
    private long id;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    private String login;
    private String name;
    @NotNull
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();
}
