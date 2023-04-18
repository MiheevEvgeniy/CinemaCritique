package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre {
    @Builder.Default
    private long id = 1;
    private String name;
}
