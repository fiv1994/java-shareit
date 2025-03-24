package ru.practicum.shareit.item.service.validators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.validation.AccessDeniedException;
import ru.practicum.shareit.user.service.validation.NotFoundException;
import ru.practicum.shareit.user.service.validation.UserValidator;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemValidator {
    private final ItemRepository itemRepository;
    private final UserValidator userValidator;

    public void validateNewItem(Item item) {
        userValidator.validateExists(item.getOwner().getId());
    }

    public void validatePatchedItem(Item item, long userId) {
        userValidator.validateExists(userId);

        long itemId = item.getId();
        validateExists(itemId);

        validateUserIsOwner(itemId, userId);
    }

    public void validateExists(long id) {
        NotFoundException exc = new NotFoundException(String.format("Item with id = %d not found.", id));
        UserValidator.throwExceptionIfTrue(!itemRepository.existsById(id), exc);
    }

    private void validateUserIsOwner(long itemId, long userId) {
        AccessDeniedException exc = new AccessDeniedException("User is not owner of item");
        long ownerIdOfItem = itemRepository.findById(itemId).get().getOwner().getId();
        UserValidator.throwExceptionIfTrue(ownerIdOfItem != userId, exc);
    }
}
