package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingDtoMapper {
    private final ItemDtoMapper itemDtoMapper;

    public Booking transformToBooking(ToServerBookingDto dto, long userId) {
        Item bookingItem = (new Item()).withId(dto.getItemId());
        User bookingCreator = (new User()).withId(userId);
        Booking result = new Booking();
        result.setItem(bookingItem);
        result.setCreator(bookingCreator);
        result.setStart(dto.getStart());
        result.setEnd(dto.getEnd());
        return result;
    }

    public FromServerBookingDto transformToFromServerDto(Booking booking) {
        FromServerBookingDto result = new FromServerBookingDto();
        result.setId(booking.getId());
        result.setBooker(UserDtoMapper.transformToNestedDto(booking.getCreator()));
        result.setItem(itemDtoMapper.transformToNestedDto(booking.getItem()));
        result.setStart(booking.getStart());
        result.setEnd(booking.getEnd());
        result.setStatus(booking.getStatus());
        return result;
    }

    public List<FromServerBookingDto> transformToFromServerDto(List<Booking> bookingsList) {
        return bookingsList.stream().map(this::transformToFromServerDto).toList();
    }
}