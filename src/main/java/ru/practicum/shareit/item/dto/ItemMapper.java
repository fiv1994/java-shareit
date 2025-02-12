package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {
    public static ItemDto transformToDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static List<ItemDto> transformToDto(List<Item> itemsList) {
        return itemsList.stream().map(ItemMapper::transformToDto).toList();
    }
}