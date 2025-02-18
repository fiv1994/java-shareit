package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserDtoMapper {
    public static UserDto transformToDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }
}
