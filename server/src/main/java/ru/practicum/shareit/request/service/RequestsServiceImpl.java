package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.validators.ItemValidator;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestsServiceImpl implements RequestsService {
    private final RequestRepository requestRepository;
    private final RequestsValidator requestsValidator;
    private final ItemValidator itemValidator;
    private final ItemRepository itemRepository;

    public ItemRequest createRequest(ItemRequest request) {
        requestsValidator.validateNew(request);
        ItemRequest requestToSave = new ItemRequest(request.getCreatorId(), request.getDescription());
        long createdRequestId = requestRepository.save(requestToSave).getId();
        return requestRepository.findById(createdRequestId).get();
    }

    public ItemRequest addResponseOnRequest(long requestId, long itemId) {
        itemValidator.validateExists(itemId);
        requestsValidator.validateExists(requestId);
        Item item = itemRepository.findById(itemId).get();
        ItemRequest requestToUpdate = requestRepository.findById(requestId).get();
        List<Item> answers = requestToUpdate.getItems();
        if (!answers.contains(item)) {
            answers.add(item);
            requestRepository.save(requestToUpdate);
        }
        return getRequest(requestId);
    }

    public List<ItemRequest> getRequestsOfUser(long userId) {
        return requestRepository.findByCreatorIdOrderByCreatedDesc(userId);
    }

    public List<ItemRequest> getRequestsOfOthers(long userId) {
        return requestRepository.findByCreatorIdNotOrderByCreatedDesc(userId);
    }

    public ItemRequest getRequest(long id) {
        requestsValidator.validateExists(id);
        return requestRepository.findById(id).get();
    }
}
