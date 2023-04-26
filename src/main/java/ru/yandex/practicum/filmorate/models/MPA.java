package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MPA {
    long id;
    String name;
}
