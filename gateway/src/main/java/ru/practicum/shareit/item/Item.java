package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.user.User;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private long id;

    private User owner;

    private String name;

    private String description;

    private Boolean available;
}