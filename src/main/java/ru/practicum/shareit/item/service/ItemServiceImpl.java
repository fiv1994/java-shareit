package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentsRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.validation.CommentsValidator;
import ru.practicum.shareit.item.service.validation.ItemValidator;
import ru.practicum.shareit.user.service.validation.UserValidator;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemValidator itemValidator;
    private final UserValidator userValidator;
    private final ItemRepository itemRepository;
    private final CommentsRepository commentsRepository;
    private final CommentsValidator commentsValidator;

    public Item createItem(Item item) {
        log.debug("Launched ItemService#createItem(...)");
        itemValidator.validateNewItem(item);
        Item createdItem = itemRepository.save(item);
        log.debug("Ended ItemService#createItem(...)");
        return createdItem;
    }

    public Item patchItem(Item item, long userId) {
        log.debug("Launched ItemService#patchItem(...)");
        itemValidator.validatePatchedItem(item, userId);

        long itemId = item.getId();
        Item itemFromStorage = itemRepository.findById(itemId).get();
        item.setOwner(itemFromStorage.getOwner());

        if (item.getName() == null) {
            item.setName(itemFromStorage.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemFromStorage.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(itemFromStorage.getAvailable());
        }

        Item patchedItem = itemRepository.save(item);
        log.debug("Ended ItemService#patchItem(...)");
        return patchedItem;
    }

    public Item getItem(long id) {
        log.debug("Launched ItemService#getItem(...)");
        itemValidator.validateExists(id);
        log.debug("Ended ItemService#getItem(...)");
        return itemRepository.findById(id).get();
    }

    public List<Item> getItemsOfUser(long userId) {
        log.debug("Launched ItemService#getItemsOfUser(...)");
        userValidator.validateExists(userId);
        List<Item> itemsOfUser = itemRepository.findByOwnerId(userId);
        log.debug("Ended ItemService#getItemOfUser(...)");
        return itemsOfUser;
    }

    public List<Item> searchAvailableItems(String text) {
        log.debug("Launched ItemService#searchAvailableItems(...)");
        List<Item> availableItems;
        if (text.isEmpty()) {
            availableItems = List.of();
        } else {
            availableItems = itemRepository.findAvailableWhoseNameOrDescContainsText(text);
        }
        log.debug("Ended ItemService#searchAvailableItems(...)");
        return availableItems;
    }

    public Comment createComment(Comment comment) {
        log.debug("Launched ItemService#createComment(...)");
        commentsValidator.validateNewComment(comment);
        Comment result = commentsRepository.save(comment);
        log.debug("Ended ItemService#createComment(...)");
        return result;
    }
}
