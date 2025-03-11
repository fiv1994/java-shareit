package ru.practicum.shareit.item.service.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingFilterValues;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.service.validation.RequestParamIncorrectOrAbsentException;
import ru.practicum.shareit.user.service.validation.UserValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentsValidator {
    private final BookingService bookingService;

    public void validateNewComment(Comment comment) {
        validateText(comment.getText());
        validateWasBooking(comment.getAuthorId(), comment.getItemId());
    }

    private void validateText(String content) {
        boolean condition = UserValidator.isStringEmptyInJson(content);
        RequestParamIncorrectOrAbsentException exc =
                new RequestParamIncorrectOrAbsentException("Content can not be empty or contain only whitespaces.");
        UserValidator.throwExceptionIfTrue(condition, exc);
    }

    private void validateWasBooking(long userId, long itemId) {
        List<Booking> userPastBookings =
                bookingService.getUsersBookings(BookingFilterValues.PAST, userId, Sort.Direction.ASC);
        boolean condition =
                userPastBookings.stream().noneMatch(booking -> booking.getItem().getId() == itemId);
        String excMessageTemplate = "User (id = %d) didn't make bookings for item(id = %d)";
        String excMessage = String.format(excMessageTemplate, userId, itemId);
        RequestParamIncorrectOrAbsentException exc = new RequestParamIncorrectOrAbsentException(excMessage);
        UserValidator.throwExceptionIfTrue(condition, exc);
    }
}
