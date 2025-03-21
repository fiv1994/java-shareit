package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.validation.BookingsValidator;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.validators.ItemValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.validation.NotFoundException;
import ru.practicum.shareit.user.service.validation.UserValidator;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class BookingsValidatorUnitTests {
    @MockBean
    private ItemValidator itemValidator;
    @MockBean
    private UserValidator userValidator;
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private BookingsValidator bookingsValidator;

    @Test
    public void validateNewBookingShouldPassWhenDataIsCorrect() {
        User owner = new User(1L, "Jim", "rand@mail.ru");
        Item item = new Item(1L, owner, "Chair", "Desc", true);

        User booker = new User(2L, "John", "rand.aee@mail.ru");
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(4);
        Booking booking = new Booking(0, booker, item, start, end, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.bookingExists(Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class), Mockito.anyLong()))
                .thenReturn(false);

        Assertions.assertDoesNotThrow(() -> bookingsValidator.validateNewBooking(booking, booker.getId()));
    }

    @Test
    public void validateNewBookingShouldNotPassWhenItemIsAbsent() {
        Booking booking = generateCorrectBooking();
        Mockito.doThrow(NotFoundException.class)
                .when(itemValidator)
                .validateExists(Mockito.anyLong());

        Assertions.assertThrows(NotFoundException.class,
                () -> bookingsValidator.validateNewBooking(booking, 1L));
    }

    @Test
    public void validateNewBookingShouldNotPassWhenUserIsAbsent() {
        Booking booking = generateCorrectBooking();
        Mockito.doThrow(NotFoundException.class)
                .when(userValidator)
                .validateExists(Mockito.anyLong());

        Assertions.assertThrows(NotFoundException.class,
                () -> bookingsValidator.validateNewBooking(booking, 1L));
    }

    private Booking generateCorrectBooking() {
        User owner = new User(1L, "Jim", "rand@mail.ru");
        Item item = new Item(1L, owner, "Chair", "Desc", true);

        User booker = new User(2L, "John", "rand.aee@mail.ru");
        LocalDateTime start = LocalDateTime.now().plusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(4);
        return new Booking(0, booker, item, start, end, BookingStatus.WAITING);
    }
}
