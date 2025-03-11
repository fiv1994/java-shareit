package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

    @Query(value = """
            SELECT EXISTS (SELECT 1
                           FROM bookings
                           WHERE item_id = :itemId AND
                           bookings.start_timestamp >= :startDate AND
                           bookings.end_timestamp <= :endDate);
            """,
            nativeQuery = true)
    Boolean bookingExists(LocalDateTime startDate, LocalDateTime endDate, long itemId);

    @Query(value = """
            SELECT MAX(start_timestamp) AS start,
                   MAX(end_timestamp) AS end,
                   item_id
            FROM bookings
            WHERE item_id IN :itemIds
            AND NOT start_timestamp > :now
            GROUP BY item_id;
            """,
            nativeQuery = true)
    List<BookingShort> lastBookingsOfItems(Collection<Long> itemIds, LocalDateTime now);

    @Query(value = """
            SELECT MIN(start_timestamp) AS start,
                   MIN(end_timestamp) AS end,
                   item_id
            FROM bookings
            WHERE item_id IN :itemIds
            AND start_timestamp > :now
            GROUP BY item_id;
            """,
            nativeQuery = true)
    List<BookingShort> nearestFutureBookingsOfItems(Collection<Long> itemIds, LocalDateTime now);
}
