package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item);

    Item patchItem(Item item, long userId);

    Item getItem(long id);

    List<Item> getItemsOfUser(long userId);

    List<Item> searchAvailableItems(String text);

    Comment createComment(Comment comment);
}