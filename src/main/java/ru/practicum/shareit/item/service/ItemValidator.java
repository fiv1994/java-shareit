package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.validation.AccessDeniedException;
import ru.practicum.shareit.user.service.validation.NotFoundException;
import ru.practicum.shareit.user.service.validation.RequestParamIncorrectOrAbsentException;
import ru.practicum.shareit.user.service.validation.UserValidator;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemValidator {
    private final ItemRepository itemRepository;
    private final UserValidator userValidator;

    public void validateNewItem(Item item) {
        userValidator.validateExists(item.getOwnerId());
        validateNameOrDescription(item.getName());
        validateNameOrDescription(item.getDescription());
        validateAvailable(item.getAvailable());
    }

    public void validatePatchedItem(Item item, long userId) {
        userValidator.validateExists(userId);

        long itemId = item.getId();
        validateUserIsOwner(itemId, userId);

        String itemName = item.getName();
        validateNameOrDescriptionIfNotNull(itemName);

        String itemDescription = item.getDescription();
        validateNameOrDescriptionIfNotNull(itemDescription);
    }

    public void validateExists(long id) {
        NotFoundException exc = new NotFoundException(String.format("Item with id = %d not found.", id));
        UserValidator.throwExceptionIfTrue(itemRepository.get(id) == null, exc);
    }

    private void validateNameOrDescription(String itemAttr) {
        String errorMsg = "Name and Description can not be empty or contain only whitespaces.";
        RequestParamIncorrectOrAbsentException exc = new RequestParamIncorrectOrAbsentException(errorMsg);
        UserValidator.throwExceptionIfTrue(UserValidator.isStringEmptyInJson(itemAttr), exc);
    }

    private void validateNameOrDescriptionIfNotNull(String itemAttr) {
        if (itemAttr != null) {
            validateNameOrDescription(itemAttr);
        }
    }

    private void validateAvailable(Boolean available) {
        RequestParamIncorrectOrAbsentException exc =
                new RequestParamIncorrectOrAbsentException("Available field must be set.");
        UserValidator.throwExceptionIfTrue(available == null, exc);
    }

    private void validateUserIsOwner(long itemId, long userId) {
        AccessDeniedException exc = new AccessDeniedException("User is not owner of item");
        UserValidator.throwExceptionIfTrue(itemRepository.get(itemId).getOwnerId() != userId, exc);
    }
}
