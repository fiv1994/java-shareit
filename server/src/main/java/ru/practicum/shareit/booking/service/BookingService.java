package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, long userId);

    Booking permitOrRejectBooking(long bookingId, boolean approve, long itemOwnerId);

    Booking getBooking(long bookingId, long userId);

    List<Booking> getUsersBookings(BookingsFilterValues bookingFilterValue,
                                   long userId,
                                   Sort.Direction sortOrder);

    List<Booking> getBookingsOfUsersItems(BookingsFilterValues bookingFilterValue,
                                          long userId,
                                          Sort.Direction sortOrder);
}
