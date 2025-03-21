package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid ItemRequest request) {
        log.info("Started request handling by ItemRequestController#createRequest(...)");
        return itemRequestClient.createRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Started request handling by ItemRequestController#getRequestsOfUser(...)");
        return itemRequestClient.getRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsNotFromUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Started request handling by ItemRequestController#getRequestsNotFromUser (...)");
        return itemRequestClient.getRequestsNotFromUser (userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable long requestId) {
        log.info("Started request handling by ItemRequestController#getItemRequest(...)");
        return itemRequestClient.getItemRequest(userId, requestId);
    }
}