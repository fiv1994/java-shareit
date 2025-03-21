package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(of = {"id"})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private long id;

    private long creatorId;

    @NotBlank
    private String description;

    private @With LocalDateTime created;

    List<Item> items;

    public ItemRequest(long creatorId, String description) {
        this.id = 0;
        this.creatorId = creatorId;
        this.description = description;
        this.created = LocalDateTime.now();
        this.items = new ArrayList<>();
    }
}