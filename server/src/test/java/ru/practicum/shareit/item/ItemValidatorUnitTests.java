package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.validators.ItemValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.validation.NotFoundException;
import ru.practicum.shareit.user.service.validation.UserValidator;

import java.util.Optional;

@SpringBootTest
public class ItemValidatorUnitTests {

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserValidator userValidator;

    @Autowired
    private ItemValidator itemValidator;

    @Test
    public void checkValidateExistsThrowsExceptionWhenItemIsAbsent() {
        Mockito.when(itemRepository.existsById(3L))
                .thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> itemValidator.validateExists(3L));
    }

    @Test
    public void checkValidateExistsDoesNotThrowExceptionWhenItemExists() {
        Mockito.when(itemRepository.existsById(3L))
                .thenReturn(true);
        Assertions.assertDoesNotThrow(() -> itemValidator.validateExists(3L));
    }

    @Test
    public void validateNewItemShouldNotThrowExcWhenDataIsCorrect() {
        User user = new User(1L, null, null);
        Item item = new Item(1L, user, "Table", "Very good table.", true);
        Assertions.assertDoesNotThrow(() -> itemValidator.validateNewItem(item));
    }

    @Test
    public void validationOfNewItemShouldNotPassWhenUserNotExists() {
        User user = new User(1L, null, null);
        Item item = new Item(1L, user, "Table", "Very good table.", true);
        Mockito.doThrow(new NotFoundException("No"))
                .when(userValidator)
                .validateExists(Mockito.anyLong());
        Assertions.assertThrows(NotFoundException.class, () -> itemValidator.validateNewItem(item));
    }

    @Test
    public void validatePatchedItemShouldNotThrowExcWhenDataIsCorrect() {
        Mockito.when(itemRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);
        User user = new User(1L, null, null);
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new Item(1L, user, "Table", "Good table.", true)));
        Item item = new Item(1L, null, "Table", "Very good table.", true);
        Assertions.assertDoesNotThrow(() -> itemValidator.validatePatchedItem(item, 1L));
    }

    @Test
    public void validatePatchedItemShouldThrowExcWhenUserIsAbsent() {
        Mockito.doThrow(new NotFoundException("Not found"))
                .when(userValidator).validateExists(Mockito.anyLong());
        Item item = new Item(1L, null, "Table", "Very good table.", true);
        Assertions.assertThrows(NotFoundException.class, () -> itemValidator.validatePatchedItem(item, 1L));
    }

    @Test
    public void validatePatchedItemShouldThrowExcWhenItemIsAbsent() {
        Mockito.when(itemRepository.existsById(1L))
                .thenReturn(false);
        Item item = new Item(1L, null, "Table", "Very good table.", true);
        Assertions.assertThrows(NotFoundException.class, () -> itemValidator.validatePatchedItem(item, 1L));
    }

    @Test
    public void validatePatchedItemShouldThrowExcWhenUserIsNotOwner() {
        User user = new User(1L, "Bob", "Bob@mail.ru");
        Item item = new Item(1L, user, "Table", "Very good table.", true);
        Mockito.when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        Assertions.assertThrows(NotFoundException.class, () -> itemValidator.validatePatchedItem(item, 2L));
    }

    @Test
    public void validatePatchedItemShouldThrowExcWhenNameIsIncorrect() {
        Item item = new Item(1L, null, "   ", "Very good table.", true);
        Assertions.assertThrows(NotFoundException.class, () -> itemValidator.validatePatchedItem(item, 1L));
    }

    @Test
    public void validatePatchedItemShouldThrowExcWhenDescriptionIsIncorrect() {
        Item item = new Item(1L, null, "Table", "    ", true);
        Assertions.assertThrows(NotFoundException.class, () -> itemValidator.validatePatchedItem(item, 1L));
    }
}