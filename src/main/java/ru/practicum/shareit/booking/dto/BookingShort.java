package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface BookingShort {
    LocalDateTime getStart();

    LocalDateTime getEnd();

    @Value("#{target.item_id}")
    long getItemId();
}