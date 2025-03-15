package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, long userId);

    Booking permitOrRejectBooking(long bookingId, boolean approve, long itemOwnerId);

    Booking getBooking(long bookingId, long userId);

    List<Booking> getUsersBookings(BookingFilterValues bookingFilterValue,
                                   long userId,
                                   Sort.Direction sortOrder);

    List<Booking> getBookingsOfUsersItems(BookingFilterValues bookingFilterValue,
                                          long userId,
                                          Sort.Direction sortOrder);
}
