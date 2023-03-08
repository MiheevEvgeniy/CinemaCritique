package ru.yandex.practicum.filmorate.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    @NotNull
    private String name;
    private String description;
    private long id;
    @Builder.Default
    @NotNull
    private LocalDate releaseDate = LocalDate.now();
    @Builder.Default
    @NotNull
    private double duration = 0.0;
}
