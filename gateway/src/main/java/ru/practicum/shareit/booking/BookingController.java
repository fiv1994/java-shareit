package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid ToServerBookingDto toServerBookingDto) {
        log.info("Started request handling by BookingController#createBooking(...)");
        return bookingClient.createBooking(userId, toServerBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> permitOrRejectBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @PathVariable long bookingId,
                                                        @RequestParam boolean approved) {
        log.info("Started request handling by BookingController#permitOrRejectBooking(...)");
        return bookingClient.permitOrRejectBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId) {
        log.info("Started request handling by BookingController#getBooking(...)");
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsersBookingsInAscOrder(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @RequestParam(defaultValue = "ALL") String state) {
        log.info("Started request handling by BookingController#getUsersBookingsInAscOrder(...)");
        return bookingClient.getUsersBookingsInAscOrder(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsOfUsersItemsInAscOrder(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Started request handling by BookingController#getBookingsOfUsersItemsInAscOrder(...)");
        return bookingClient.getBookingsOfUsersItemsInAscOrder(userId, state);
    }
}