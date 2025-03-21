package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.dto.FromServerBookingDto;
import ru.practicum.shareit.booking.dto.ToServerBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingsFilterValues;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingsController {
    private final BookingService bookingService;
    private final BookingDtoMapper bookingDtoMapper;

    @PostMapping
    public FromServerBookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody ToServerBookingDto toServerBookingDto) {
        log.info("Started request handling by BookingController#createBooking(...)");
        log.info("Started creating item's (id = {}) booking for user(id = {})", toServerBookingDto.getItemId(), userId);
        Booking bookingToCreate = bookingDtoMapper.transformToBooking(toServerBookingDto);
        Booking createdBooking = bookingService.createBooking(bookingToCreate, userId);
        log.info("Booking with id = {} of item (id = {}) created for user(id = {}).",
                createdBooking.getId(),
                createdBooking.getItem().getId(),
                createdBooking.getCreator().getId()
        );
        return bookingDtoMapper.transformToFromServerDto(createdBooking);
    }

    @PatchMapping("/{bookingId}")
    public FromServerBookingDto permitOrRejectBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @PathVariable long bookingId,
                                                      @RequestParam boolean approved) {
        log.info("Started request handling by BookingController#permitOrRejectBooking(...)");
        log.info("Started setting status of booking (id = {}) by user (id = {})", bookingId, userId);
        Booking approvedBooking = bookingService.permitOrRejectBooking(bookingId, approved, userId);
        log.info("Booking with id = {} got status {} from user(id = {}).",
                approvedBooking.getId(),
                approvedBooking.getStatus(),
                userId
        );
        return bookingDtoMapper.transformToFromServerDto(approvedBooking);
    }

    @GetMapping("/{bookingId}")
    public FromServerBookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable long bookingId) {
        log.info("Started request handling by BookingController#getBooking(...)");
        log.info("Started getting  booking (id = {}) for user (id = {})", bookingId, userId);
        Booking booking = bookingService.getBooking(bookingId, userId);
        log.info("Booking with id = {} found", booking.getId());
        return bookingDtoMapper.transformToFromServerDto(booking);
    }

    @GetMapping
    public List<FromServerBookingDto> getUsersBookingsInAscOrder(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                 @RequestParam(defaultValue = "ALL") BookingsFilterValues state) {
        log.info("Started request handling by BookingController#getUsersBookingsInAscOrder(...)");
        log.info("Started getting  bookings in state {} for user (id = {})", state, userId);
        List<Booking> bookings = bookingService.getUsersBookings(state, userId, Sort.Direction.ASC);
        log.info("Bookings in state {} for user (id = {}) found", state, userId);
        return bookingDtoMapper.transformToFromServerDto(bookings);
    }

    @GetMapping("/owner")
    public List<FromServerBookingDto> getBookingsOfUsersItemsInAscOrder(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL") BookingsFilterValues state) {
        log.info("Started request handling by BookingController#getBookingsOfUsersItemsInAscOrder(...)");
        log.info("Started getting  bookings in state {} for user's (id = {}) items", state, userId);
        List<Booking> bookings = bookingService.getBookingsOfUsersItems(state, userId, Sort.Direction.ASC);
        log.info("Bookings in state {} for user's (id = {}) items found", state, userId);
        return bookingDtoMapper.transformToFromServerDto(bookings);
    }
}