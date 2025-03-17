package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserDtoMapper {
    public static FromServerUserDto transformToFromServerDto(User user) {
        return new FromServerUserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static UserDtoInFromServerBookingDto transformToNestedDto(User user) {
        return new UserDtoInFromServerBookingDto(user.getId());
    }
}