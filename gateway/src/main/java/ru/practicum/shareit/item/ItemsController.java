package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemsController {
    private final ItemsClient itemsClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid ToServerItemDto itemDto) {
        log.info("Started request handling by ItemController#createItem(...)");
        return itemsClient.createItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId) {
        log.info("Started request handling by ItemController#getItem(...)");
        return itemsClient.getItem(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Started request handling by ItemController#getItemsOfUser(...)");
        return itemsClient.getItemsOfUser(userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@PathVariable("itemId") String itemIdString,
                                            @RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestBody Item item
    ) {
        log.info("Started request handling by ItemController#patchItem(...)");
        validateStringNotBlank(item.getName());
        validateStringNotBlank(item.getDescription());
        return itemsClient.patchItem(itemIdString, userId, item);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchAvailableItems(@RequestParam @NotBlank String text) {
        log.info("Started request handling by ItemController#searchItems(...)");
        return itemsClient.searchAvailableItems(text);
    }

    private void validateStringNotBlank(@NotBlank String str) {
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable("itemId") long itemId,
                                                @RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid Comment comment
    ) {
        log.info("Started request handling by ItemController#createComment(...)");
        return itemsClient.createComment(itemId, userId, comment);
    }
}
