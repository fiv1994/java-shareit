package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Service
public interface RequestsService {
    ItemRequest createRequest(ItemRequest request);

    ItemRequest addResponseOnRequest(long requestId, long itemId);

    List<ItemRequest> getRequestsOfUser(long userId);

    List<ItemRequest> getRequestsOfOthers(long userId);

    ItemRequest getRequest(long id);
}
