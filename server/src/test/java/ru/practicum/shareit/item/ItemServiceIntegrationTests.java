package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.validation.NotFoundException;

import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceIntegrationTests {

    private final ItemService itemService;
    private final UserService userService;

    @Test
    public void createItemShouldWorkWhenDataIsCorrect() {
        long userId = createUser();
        Item itemToCreate = new Item(0L,
                new User(userId, null, null),
                "Chair",
                "Desc",
                true);
        Item createdItem = itemService.createItem(itemToCreate, null);
        boolean condition1 = createdItem.getName().equals(itemToCreate.getName());
        boolean condition2 = createdItem.getDescription().equals(itemToCreate.getDescription());
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void createItemShouldNotWorkWhenValidationNotPassed() {
        Item item = new Item(0L,
                new User(33L, null, null),
                "Chair",
                "Desc",
                true);
        Assertions.assertThrows(NotFoundException.class, () -> itemService.createItem(item, null));
    }

    @Test
    public void patchItemShouldWorkWhenDataIsCorrect() {
        long userId = createUser();
        Item itemToCreate = new Item(0L,
                new User(userId, null, null),
                "Chair",
                "Desc1",
                true);
        long createdItemId = itemService.createItem(itemToCreate, null).getId();

        String newDescription = "Desc2";
        Item patchedItem = new Item(createdItemId, null, null, newDescription, true);
        Item result = itemService.patchItem(patchedItem, userId);
        boolean condition1 = result.getName().equals(itemToCreate.getName());
        boolean condition2 = result.getDescription().equals(newDescription);
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void patchItemShouldNotWorkWhenValidationNotPassed() {
        Item item = new Item(1L,
                null,
                "Chair",
                "Desc1",
                true);

        Assertions.assertThrows(NotFoundException.class, () -> itemService.patchItem(item, 2L));
    }

    @Test
    public void getItemShouldWorkWhenValidationPassed() {
        long createdUserId = createUser();
        Item itemToCreate = new Item(0L,
                new User(createdUserId, null, null),
                "Chair",
                "Desc1",
                true);
        long createdItemId = itemService.createItem(itemToCreate, null).getId();
        Item result = itemService.getItem(createdItemId);
        boolean condition1 = result.getName().equals(itemToCreate.getName());
        boolean condition2 = result.getDescription().equals(itemToCreate.getDescription());

        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void getItemShouldNotWorkWhenValidationFailed() {
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItem(1L));
    }

    @Test
    public void getItemsOfUserShouldWorkWhenValidationPassed() {
        long userId = createUser();
        Item firstItemToCreate = new Item(0L,
                new User(userId, null, null),
                "Chair1",
                "Desc1",
                true);
        long firstItemId = itemService.createItem(firstItemToCreate, null).getId();

        Item secondItemToCreate = new Item(0L,
                new User(userId, null, null),
                "Chair2",
                "Desc2",
                true);
        long secondItemId = itemService.createItem(secondItemToCreate, null).getId();

        List<Item> result = itemService.getItemsOfUser(userId);
        boolean condition1 = result.size() == 2;
        boolean condition2 = result.getFirst().getId() == firstItemId;
        boolean condition3 = result.getLast().getId() == secondItemId;

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    @Test
    public void getItemsOfUserShouldNotWorkWhenValidationFailed() {
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemsOfUser(1L));
    }

    @Test
    public void searchAvailableItemsShouldReturnEmptyListWhenTextIsEmpty() {
        Assertions.assertTrue(itemService.searchAvailableItems("").isEmpty());
    }

    private long createUser() {
        User userToCreate = new User(0L, "Bob", "bob@mail.ru");
        return userService.createUser(userToCreate).getId();
    }
}
