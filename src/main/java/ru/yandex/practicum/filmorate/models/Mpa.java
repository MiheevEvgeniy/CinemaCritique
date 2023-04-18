package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Mpa {
    @Builder.Default
    private long id = 1;
    private String name;
}
