package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ToServerBookingDto {
    private long itemId;

    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
}
