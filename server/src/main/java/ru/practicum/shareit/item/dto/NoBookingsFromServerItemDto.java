package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class NoBookingsFromServerItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private List<FromServerCommentDto> comments;
}