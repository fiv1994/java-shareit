package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.practicum.shareit.user.User;

@Getter
public class ToServerItemDto {
    private long id;

    private User owner;


    @NotBlank(message = "Name of item can not be blank.")
    private String name;

    @NotBlank(message = "Description of user can not be blank.")
    private String description;

    @NotNull
    private Boolean available;

    private long requestId;
}
