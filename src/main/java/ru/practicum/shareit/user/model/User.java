package ru.practicum.shareit.user.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
public class User {
    private @With long id;
    private String name;
    private String email;
}
