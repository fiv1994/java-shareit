package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.validators.ItemValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.validation.NotFoundException;
import ru.practicum.shareit.user.service.validation.UserValidator;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceUnitTests {
    @MockBean
    private ItemValidator itemValidator;
    @MockBean
    private UserValidator userValidator;
    @MockBean
    private ItemRepository itemRepository;

    private final ItemService itemService;

    @Test
    public void createItemShouldWorkWhenDataIsCorrect() {
        User user = new User(1L, "Jim", "rand@mail.ru");
        Item item = new Item(0L, user, "Chair", "Desc", true);
        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenAnswer(invocation -> imitateSaving(invocation, item, 1L));
        boolean condition = itemsFieldsAreEqual(item.withId(1), itemService.createItem(item, null));
        Assertions.assertTrue(condition);
    }

    @Test
    public void createItemShouldNotWorkWhenValidationNotPassed() {
        User user = new User(1L, "Jim", "rand@mail.ru");
        Item item = new Item(0L, user, "Chair", "Desc", true);
        Mockito.doThrow(new RuntimeException("Validation not passed"))
                .when(itemValidator).validateNewItem(Mockito.any(Item.class));
        Assertions.assertThrows(RuntimeException.class, () -> itemService.createItem(item, null));
    }

    @Test
    public void patchItemShouldWorkWhenDataIsCorrect() {
        long id = 1L;
        User user = new User(id, "Jim", "rand@mail.ru");
        Item itemInDb = new Item(id, user, "Chair", "Desc1", true);

        String newDescription = "Desc2";
        Item patchedItem = new Item(itemInDb.getId(), null, null, newDescription, true);

        Mockito.when(itemRepository.findById(id))
                .thenReturn(Optional.of(itemInDb));
        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenAnswer(invocation -> imitatePatching(invocation, itemInDb, newDescription));

        Item result = itemService.patchItem(patchedItem, itemInDb.getOwner().getId());
        Assertions.assertTrue(itemsFieldsAreEqual(itemInDb.withDescription(newDescription), result));
    }

    @Test
    public void patchItemShouldNotWorkWhenValidationNotPassed() {
        Item item = new Item(1L, null, "Chair", "Desc1", true);

        Mockito.doThrow(new NotFoundException("Not found"))
                .when(itemValidator)
                .validatePatchedItem(Mockito.any(Item.class), Mockito.anyLong());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.patchItem(item, 2L));
    }

    @Test
    public void getItemShouldWorkWhenValidationPassed() {
        User user = new User(2L, "Jim", "rand@mail.ru");
        Item item = new Item(1L, user, "Chair", "Desc1", true);

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));

        Assertions.assertEquals(item, itemService.getItem(item.getId()));
    }

    @Test
    public void getItemShouldNotWorkWhenValidationFailed() {
        Mockito.doThrow(new NotFoundException("Not found")).when(itemValidator).validateExists(Mockito.anyLong());
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItem(1L));
    }

    @Test
    public void getItemsOfUserShouldWorkWhenValidationPassed() {
        long userId = 5L;
        User user = new User(userId, "Jim", "rand@mail.ru");
        long firstItemId = 1L;
        Item item1 = new Item(firstItemId, user, "Chair1", "Desc1", true);
        long secondItemId = 2L;
        Item item2 = new Item(secondItemId, user, "Chair2", "Desc2", true);

        Mockito.when(itemRepository.findByOwnerId(Mockito.anyLong()))
                .thenReturn(List.of(item1, item2));

        List<Item> result = itemService.getItemsOfUser(userId);
        boolean condition1 = result.size() == 2;
        boolean condition2 = result.getFirst().getId() == firstItemId;
        boolean condition3 = result.getLast().getId() == secondItemId;

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    @Test
    public void getItemsOfUserShouldNotWorkWhenValidationFailed() {
        Mockito.doThrow(NotFoundException.class)
                .when(userValidator)
                .validateExists(Mockito.anyLong());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemsOfUser(1L));
    }

    @Test
    public void searchAvailableItemsShouldReturnEmptyListWhenTextIsEmpty() {
        Assertions.assertTrue(itemService.searchAvailableItems("").isEmpty());
    }

    private Item imitatePatching(InvocationOnMock inv, Item itemInDb, String newDescription) {
        Item itemFromParams = inv.getArgument(0, Item.class);
        Item correctItem = itemInDb.withDescription(newDescription);
        if (itemsFieldsAreEqual(itemFromParams, correctItem)) {
            return correctItem;
        } else {
            throw new RuntimeException("Not correct scenario");
        }
    }

    private Item imitateSaving(InvocationOnMock inv, Item correctItem, long idToSet) {
        Item itemFromParams = inv.getArgument(0, Item.class);
        if (itemsFieldsAreEqual(correctItem, itemFromParams)) {
            return itemFromParams.withId(idToSet);
        } else {
            throw new RuntimeException("Not correct scenario");
        }
    }

    private boolean itemsFieldsAreEqual(Item item1, Item item2) {
        boolean condition1 = item1.getId() == item2.getId();
        boolean condition2 = Objects.equals(item1.getName(), item2.getName());
        boolean condition3 = Objects.equals(item1.getDescription(), item2.getDescription());
        boolean condition4 = Objects.equals(item1.getAvailable(), item2.getAvailable());

        User owner1 = item1.getOwner();
        User owner2 = item2.getOwner();
        boolean condition5 = Objects.equals(owner1, owner2);
        if (condition5 && owner1 != null) {
            boolean subCondition1 = Objects.equals(owner1.getName(), owner2.getName());
            boolean subCondition2 = Objects.equals(owner1.getEmail(), owner2.getEmail());
            condition5 = condition5 && subCondition1 && subCondition2;
        }

        return condition1 && condition2 && condition3 && condition4 && condition5;
    }
}
