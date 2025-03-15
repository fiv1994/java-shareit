package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FromServerCommentDto {
    private long id;
    private String authorName;
    private long itemId;
    private String text;
    private LocalDateTime created;
}