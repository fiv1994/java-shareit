package ru.practicum.shareit.booking.service.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.validators.ItemValidator;
import ru.practicum.shareit.user.service.validation.AccessDeniedException;
import ru.practicum.shareit.user.service.validation.NotFoundException;
import ru.practicum.shareit.user.service.validation.RequestParamIncorrectOrAbsentException;
import ru.practicum.shareit.user.service.validation.UserValidator;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingsValidator {
    private final ItemValidator itemValidator;
    private final UserValidator userValidator;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    public void validateNewBooking(Booking booking, long userId) {
        long itemId = booking.getItem().getId();
        itemValidator.validateExists(itemId);
        userValidator.validateExists(userId);
        validateItemAvailable(itemId);
        validateStartAndEnd(booking.getStart(), booking.getEnd(), itemId);
        // Проверка, что пользователь не является владельцем вещи
        long itemOwnerId = itemRepository.findById(itemId).get().getOwner().getId();
        if (itemOwnerId == userId) {
            String excMessage = String.format("User (id = %d) cannot book their own item (itemId = %d)", userId, itemId);
            throw new AccessDeniedException(excMessage);
        }
    }

    public void validateBookingToPermitOrReject(long bookingId, long userId) {
        validateBookingExists(bookingId);
        validateUserOwnsItemFromBooking(bookingId, userId);
    }

    public void validateDataForGettingBooking(long bookingId, long userId) {
        validateBookingExists(bookingId);
        validateUserIsPermittedToSeeBooking(bookingId, userId);
    }

    private void validateBookingExists(long bookingId) {
        String excMessage = String.format("Booking with id = %d not found", bookingId);
        bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(excMessage));
    }

    private void validateUserOwnsItemFromBooking(long bookingId, long userId) {
        long bookingOwnerId = bookingRepository.findById(bookingId).get().getItem().getOwner().getId();
        boolean condition = bookingOwnerId != userId;
        String excMessageTemplate = "User (id = %d) is not owner of item from booking (bookingId = %d)";
        String excMessage = String.format(excMessageTemplate, userId, bookingId);
        AccessDeniedException exc = new AccessDeniedException(excMessage);
        UserValidator.throwExceptionIfTrue(condition, exc);
    }

    private void validateStartAndEnd(LocalDateTime start, LocalDateTime end, long itemId) {
        LocalDateTime now = LocalDateTime.now();
        boolean condition = start.isAfter(end) ||
                bookingRepository.bookingExists(start, end, itemId);
        String excMessage =
                String.format("Start date (%s) and/or end date (%s) incorrect or booking on these dates exists",
                        start, end);
        RequestParamIncorrectOrAbsentException exc = new RequestParamIncorrectOrAbsentException(excMessage);
        UserValidator.throwExceptionIfTrue(condition, exc);
    }

    private void validateUserIsPermittedToSeeBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        long creatorId = booking.getCreator().getId();
        long itemOwnerId = booking.getItem().getOwner().getId();
        boolean condition = (creatorId != userId) && (itemOwnerId != userId);
        String excMessageTemplate =
                "User (id = %d) is not owner of item from booking (bookingId = %d) or booking creator";
        String excMessage = String.format(excMessageTemplate, userId, booking.getId());
        AccessDeniedException exc = new AccessDeniedException(excMessage);
        UserValidator.throwExceptionIfTrue(condition, exc);
    }

    private void validateItemAvailable(long itemId) {
        boolean condition = !itemRepository.findById(itemId).get().getAvailable();
        String excMessage = String.format("Item (id = {}) is not available.", itemId);
        RequestParamIncorrectOrAbsentException exc = new RequestParamIncorrectOrAbsentException(excMessage);
        UserValidator.throwExceptionIfTrue(condition, exc);
    }
}
