package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    private long id;
    @Builder.Default
    @NotNull
    private LocalDate releaseDate = LocalDate.now();
    @Builder.Default
    @NotNull
    @Min(1)
    private long duration = 1;
}
