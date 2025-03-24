package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestFromServerDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.RequestsService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final RequestsService requestsService;

    @PostMapping
    public ItemRequestFromServerDto createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestBody ItemRequest request) {
        log.info("Started request handling by ItemRequestController#createRequest(...)");
        log.info("Started creating request with description {} for user(id = {})", request.getDescription(), userId);
        request.setCreatorId(userId);
        ItemRequest createdRequest = requestsService.createRequest(request);
        log.info("Request with id = {} created for user(id = {}).",
                createdRequest.getId(),
                createdRequest.getCreatorId()
        );
        return ItemRequestMapper.convertToFromServerDto(createdRequest);
    }

    @GetMapping
    public List<ItemRequestFromServerDto> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Started request handling by ItemRequestController#getRequestsOfUser(...)");
        log.info("Started getting requests for user(id = {})", userId);
        List<ItemRequest> foundRequests = requestsService.getRequestsOfUser(userId);
        log.info("Requests for user with id = {} found", userId);
        return ItemRequestMapper.convertToFromServerDtoList(foundRequests);
    }

    @GetMapping("/all")
    public List<ItemRequestFromServerDto> getRequestsNotFromUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Started request handling by ItemRequestController#getRequestsNotFromUser(...)");
        log.info("Started getting requests not from user(id = {})", userId);
        List<ItemRequest> foundRequests = requestsService.getRequestsOfOthers(userId);
        log.info("Requests not from user with id = {} found", userId);
        return ItemRequestMapper.convertToFromServerDtoList(foundRequests);
    }

    @GetMapping("/{requestId}")
    public ItemRequestFromServerDto getItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PathVariable long requestId) {
        log.info("Started request handling by ItemRequestController#getItemRequest(...)");
        log.info("Started getting request(id = {}) for user with id = {}", requestId, userId);
        ItemRequest foundRequest = requestsService.getRequest(requestId);
        log.info("Request with id = {} found", requestId);
        return ItemRequestMapper.convertToFromServerDto(foundRequest);
    }
}