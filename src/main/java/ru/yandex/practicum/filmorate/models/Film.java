package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @JsonProperty("genres")
    @Builder.Default
    private List<Genre> genres = new ArrayList<>();
    @NotNull
    @JsonProperty("mpa")
    private Mpa mpa;
    @Builder.Default
    @NotNull
    private LocalDate releaseDate = LocalDate.now();
    @Builder.Default
    @NotNull
    @Min(1)
    private long duration = 0;
    @Builder.Default
    @JsonIgnore
    private long likes = 0;
}
