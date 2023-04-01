package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    private long id;
    private int rate;
    @NotNull
    private Genres genre;
    @NotNull
    private Rating rating;
    @Builder.Default
    @NotNull
    private LocalDate releaseDate = LocalDate.now();
    @Builder.Default
    @NotNull
    @Min(1)
    private long duration = 1;
    @Builder.Default
    @JsonIgnore
    private long likes = 0;
    @JsonIgnore
    private final Set<Long> usersLiked = new HashSet<>();
}
