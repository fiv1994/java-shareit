package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequestFromServerDto {
    private long id;

    private long creatorId;

    private String description;

    private LocalDateTime created;

    private List<ItemInRequestDto> items;
}
