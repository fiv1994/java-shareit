package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingsFilterValues;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingsServiceIntegrationTests {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingsService;

    private User user1;
    private User user2;
    private Item item1;
    private Item item2;

    @Test
    public void serviceShouldCreateBookingWhenDataIsOk() {
        long userId = user2.getId();
        Booking bookingToCreate = new Booking(0L,
                null,
                item1,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4),
                BookingStatus.WAITING);

        long resultId = bookingsService.createBooking(bookingToCreate, userId).getId();
        Booking result = bookingsService.getBooking(resultId, userId);
        boolean condition1 = result.getCreator().getId() == userId;
        boolean condition2 = result.getItem().getId() == item1.getId();

        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void serviceShouldAllowItemOwnerToPermitBooking() {
        long bookingCreatorId = user2.getId();
        Booking bookingToCreate = new Booking(0L,
                null,
                item1,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4),
                BookingStatus.WAITING);

        long resultId = bookingsService.createBooking(bookingToCreate, bookingCreatorId).getId();
        Booking updatedBooking = bookingsService.permitOrRejectBooking(resultId, true, user1.getId());
        Assertions.assertEquals(updatedBooking.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    public void serviceShouldReturnUsersBookings() {
        long bookingCreatorId = user2.getId();
        Booking bookingToCreate = new Booking(0L,
                null,
                item1,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4),
                BookingStatus.WAITING);

        long resultId = bookingsService.createBooking(bookingToCreate, bookingCreatorId).getId();
        List<Booking> usersBookings = bookingsService.getUsersBookings(BookingsFilterValues.ALL,
                bookingCreatorId, Sort.Direction.ASC);
        boolean condition1 = usersBookings.size() == 1;
        boolean condition2 = usersBookings.getFirst().getId() == resultId;
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void serviceShouldReturnBookingsOfUsersItems() {
        long bookingCreatorId = user2.getId();
        long itemsOwnerId = user1.getId();
        Booking bookingToCreate = new Booking(0L,
                null,
                item1,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(4),
                BookingStatus.WAITING);

        long resultId = bookingsService.createBooking(bookingToCreate, bookingCreatorId).getId();
        List<Booking> usersItemsBookings = bookingsService.getBookingsOfUsersItems(BookingsFilterValues.ALL,
                itemsOwnerId, Sort.Direction.ASC);
        boolean condition1 = usersItemsBookings.size() == 1;
        boolean condition2 = usersItemsBookings.getFirst().getId() == resultId;
        Assertions.assertTrue(condition1 && condition2);
    }


    @BeforeEach
    public void createTwoItemsAndTwoUsers() {
        User userFstToCreate = new User(0L, "Bob", "bob@mail.ru");
        user1 = userService.createUser(userFstToCreate);

        User userSndToCreate = new User(0L, "Kate", "kate@mail.ru");
        user2 = userService.createUser(userSndToCreate);

        Item fstItemToCreate = new Item(0L,
                user1,
                "Item1",
                "Description1",
                true);

        item1 = itemService.createItem(fstItemToCreate, null);

        Item sndItemToCreate = new Item(0L,
                user1,
                "Item1",
                "Description1",
                true);
        item2 = itemService.createItem(sndItemToCreate, null);
    }
}
