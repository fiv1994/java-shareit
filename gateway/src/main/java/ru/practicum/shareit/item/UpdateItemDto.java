package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateItemDto {
    @NotBlank(message = "Name of item can not be blank.")
    private String name;

    @NotBlank(message = "Description of item can not be blank.")
    private String description;

    private Boolean available;
}