package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoInFromServerBookingDto;
import ru.practicum.shareit.user.dto.UserDtoInFromServerBookingDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FromServerBookingDto {

    private long id;

    private UserDtoInFromServerBookingDto booker;

    private ItemDtoInFromServerBookingDto item;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;
}
