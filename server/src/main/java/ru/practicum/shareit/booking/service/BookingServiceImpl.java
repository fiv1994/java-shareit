package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.validation.BookingsValidator;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.validation.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingsValidator bookingsValidator;
    private final UserValidator userValidator;

    public Booking createBooking(Booking booking, long userId) {
        bookingsValidator.validateNewBooking(booking, userId);
        booking.setStatus(BookingStatus.WAITING);
        booking.setCreator(userRepository.findById(userId).get());
        booking.setItem(itemRepository.findById(booking.getItem().getId()).get());
        Long resultId = bookingRepository.save(booking).getId();
        return bookingRepository.findById(resultId).get();
    }

    public Booking permitOrRejectBooking(long bookingId, boolean approve, long userId) {
        bookingsValidator.validateBookingToPermitOrReject(bookingId, userId);
        Booking bookingFromStorage = bookingRepository.findById(bookingId).get();
        BookingStatus bookingStatus = approve ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        bookingFromStorage.setStatus(bookingStatus);
        return bookingRepository.save(bookingFromStorage);
    }

    public Booking getBooking(long bookingId, long userId) {
        bookingsValidator.validateDataForGettingBooking(bookingId, userId);
        return bookingRepository.findById(bookingId).get();
    }

    public List<Booking> getUsersBookings(BookingsFilterValues bookingFilterValue,
                                          long userId,
                                          Sort.Direction sortOrder) {
        userValidator.validateExists(userId);
        return filterAndSortBookings("start",
                sortOrder,
                bookingFilterValue,
                QBooking.booking.creator.id.eq(userId)
        );
    }

    public List<Booking> getBookingsOfUsersItems(BookingsFilterValues bookingFilterValue,
                                                 long userId,
                                                 Sort.Direction sortOrder) {
        userValidator.validateExists(userId);
        return filterAndSortBookings("start",
                sortOrder,
                bookingFilterValue,
                QBooking.booking.item.owner.id.eq(userId)
        );
    }

    private List<Booking> filterAndSortBookings(String bookingSortingProperty,
                                                Sort.Direction sortOrder,
                                                BookingsFilterValues bookingFilterValue,
                                                BooleanExpression filterExpression) {
        Sort sort = Sort.by(sortOrder, bookingSortingProperty);
        BooleanExpression byValue = byValue(bookingFilterValue);
        BooleanExpression finalBooleanExpression = byValue == null ? filterExpression : filterExpression.and(byValue);
        Iterable<Booking> preResult = bookingRepository.findAll(finalBooleanExpression, sort);

        List<Booking> result = new ArrayList<>();
        preResult.forEach(result::add);
        return result;
    }

    private BooleanExpression byValue(BookingsFilterValues bookingFilterValue) {
        BooleanExpression byValue = null;
        LocalDateTime now = LocalDateTime.now();
        switch (bookingFilterValue) {
            case PAST:
                byValue = QBooking.booking.end.lt(now);
                break;
            case CURRENT:
                byValue = QBooking.booking.start.loe(now).and(QBooking.booking.end.goe(now));
                break;
            case FUTURE:
                byValue = QBooking.booking.start.lt(now);
                break;
            case WAITING:
                byValue = QBooking.booking.status.eq(BookingStatus.WAITING);
                break;
            case REJECTED:
                byValue = QBooking.booking.status.eq(BookingStatus.REJECTED);
                break;
        }
        return byValue;
    }
}
