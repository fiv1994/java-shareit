package ru.practicum.shareit.item.dto;


import lombok.Getter;
import ru.practicum.shareit.user.model.User;

@Getter
public class ToServerItemDto {
    private long id;

    private User owner;

    private String name;

    private String description;

    private Boolean available;
    private long requestId;
}
