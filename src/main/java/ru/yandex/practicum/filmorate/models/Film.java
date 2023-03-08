package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
