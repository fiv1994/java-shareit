package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final ItemDtoMapper itemDtoMapper;
    private final CommentDtoMapper commentDtoMapper;

    @PostMapping
    public NoBookingsFromServerItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestBody ToServerItemDto itemDto) {
        log.info("Started request handling by ItemController#createItem(...)");
        log.info("Started creating item with description = {} and name = {} for user(id = {})",
                itemDto.getDescription(), itemDto.getName(), userId);
        Item item = itemDtoMapper.transformFromToServerItemDto(itemDto);
        item.setOwner(new User(userId, null, null));
        Item createdItem = itemService.createItem(item, itemDto.getRequestId());
        log.info("Item with id = {} created for user(id = {}).", createdItem.getId(), createdItem.getOwner().getId());
        return itemDtoMapper.transformToNoBookingsDto(createdItem);
    }

    @GetMapping("/{itemId}")
    public WithBookingsFromServerItemDto getItem(@PathVariable long itemId) {
        log.info("Started request handling by ItemController#getItem(...)");
        log.info("Started extracting item with id = {}", itemId);
        Item requestedItem = itemService.getItem(itemId);
        log.info("Item with id = {} extracted", itemId);
        return itemDtoMapper.transformToWithEmptyBookingsDto(requestedItem);
    }

    @GetMapping
    public List<WithBookingsFromServerItemDto> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Started request handling by ItemController#getItemsOfUser(...)");
        log.info("Started extracting items for user with id = {}", userId);
        List<Item> requestedItems = itemService.getItemsOfUser(userId);
        log.info("Items for user with id = {} extracted", userId);
        return itemDtoMapper.transformToWithBookingsDto(requestedItems);
    }

    @PatchMapping("/{itemId}")
    public NoBookingsFromServerItemDto patchItem(@PathVariable("itemId") String itemIdString,
                                                 @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestBody Item item
    ) {
        log.info("Started request handling by ItemController#patchItem(...)");
        item.setId(Long.parseLong(itemIdString));
        log.info("Started updating item with id = {} and ownerId = {}", item.getId(), userId);
        Item result = itemService.patchItem(item, userId);
        log.info("Ended updating item with id = {} for user(id = {}) ", result.getId(), result.getOwner().getId());

        return itemDtoMapper.transformToNoBookingsDto(result);
    }

    @GetMapping("/search")
    public List<NoBookingsFromServerItemDto> searchAvailableItems(@RequestParam String text) {
        log.info("Started request handling by ItemController#searchItems(...)");
        log.info("Started searching available items with text = {} in name or description", text);
        List<Item> foundItems = itemService.searchAvailableItems(text);
        log.info("Available items with text = {} in name or description extracted", text);
        return itemDtoMapper.transformToNoBookingsDto(foundItems);
    }

    @PostMapping("/{itemId}/comment")
    public FromServerCommentDto createComment(@PathVariable("itemId") long itemId,
                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody Comment comment
    ) {
        log.info("Started request handling by ItemController#createComment(...)");
        comment.setCreated(LocalDateTime.now());
        comment.setItemId(itemId);
        comment.setAuthorId(userId);
        log.info("Started creating comment with authorId = {} and itemId = {}", userId, itemId);
        Comment result = itemService.createComment(comment);
        log.info("Comment(id = {}) by user(id = {}) for item(id = {}) created", comment.getId(),
                comment.getAuthorId(), itemId);
        return commentDtoMapper.convertToDto(result);
    }
}
