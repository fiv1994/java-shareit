package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody Item item) {
        log.info("Started request handling by ItemController#createItem(...)");
        log.info("Started creating item with description = {} and name = {} for user(id = {})",
                item.getDescription(), item.getName(), userId);
        item.setOwnerId(userId);
        Item createdItem = itemService.createItem(item);
        log.info("Item with id = {} created for user(id = {}).", createdItem.getId(), createdItem.getOwnerId());
        return ItemMapper.transformToDto(createdItem);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        log.info("Started request handling by ItemController#getItem(...)");
        log.info("Started extracting item with id = {}", itemId);
        Item requestedItem = itemService.getItem(itemId);
        log.info("Item with id = {} extracted", itemId);
        return ItemMapper.transformToDto(requestedItem);
    }

    @GetMapping
    public List<ItemDto> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Started request handling by ItemController#getItemsOfUser(...)");
        log.info("Started extracting items for user with id = {}", userId);
        List<Item> requestedItems = itemService.getItemsOfUser(userId);
        log.info("Items for user with id = {} extracted", userId);
        return ItemMapper.transformToDto(requestedItems);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@PathVariable("itemId") String itemIdString,
                             @RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestBody Item item
    ) {
        log.info("Started request handling by ItemController#patchItem(...)");
        item.setId(Long.parseLong(itemIdString));
        log.info("Started updating item with id = {} and ownerId = {}", item.getId(), userId);
        Item result = itemService.patchItem(item, userId);
        log.info("Ended updating item with id = {} for user(id = {}) ", result.getId(), result.getOwnerId());

        return ItemMapper.transformToDto(result);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItems(@RequestParam String text) {
        log.info("Started request handling by ItemController#searchItems(...)");
        log.info("Started searching available items with text = {} in name or description", text);
        List<Item> foundItems = itemService.searchAvailableItems(text);
        log.info("Available items with text = {} in name or description extracted", text);
        return ItemMapper.transformToDto(foundItems);
    }
}