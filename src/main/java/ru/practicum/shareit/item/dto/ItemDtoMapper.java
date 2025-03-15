package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ItemDtoMapper {
    private final BookingRepository bookingRepository;
    private final CommentsRepository commentsRepository;
    private final CommentDtoMapper commentDtoMapper;

    public NoBookingsFromServerItemDto transformToNoBookingsDto(Item item) {
        List<Comment> comments = commentsRepository.findByItemId(item.getId());
        List<FromServerCommentDto> commentsDtos = commentDtoMapper.convertToDto(comments);
        return new NoBookingsFromServerItemDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), commentsDtos);
    }

    public List<NoBookingsFromServerItemDto> transformToNoBookingsDto(List<Item> itemsList) {
        return itemsList.stream().map(this::transformToNoBookingsDto).toList();
    }

    public WithBookingsFromServerItemDto transformToWithBookingsDto(Item item) {
        WithBookingsFromServerItemDto result = transformToWithEmptyBookingsDto(item);

        List<BookingShort> lastBookingInList = bookingRepository.lastBookingsOfItems(List.of(item.getId()),
                LocalDateTime.now());
        if (!lastBookingInList.isEmpty()) {
            result.setLastBooking(lastBookingInList.getFirst());
        }

        List<BookingShort> nextBookingInList = bookingRepository.nearestFutureBookingsOfItems(List.of(item.getId()),
                LocalDateTime.now());
        if (!nextBookingInList.isEmpty()) {
            result.setNextBooking(nextBookingInList.getFirst());
        }

        return result;
    }

    public List<WithBookingsFromServerItemDto> transformToWithBookingsDto(List<Item> itemsList) {
        Map<Long, WithBookingsFromServerItemDto> mapWithDtos = new HashMap<>();
        itemsList.forEach(item -> mapWithDtos.put(item.getId(), transformToWithEmptyBookingsDto(item)));

        Consumer<BookingShort> setLastBooking =
                bookingShort -> {
                    mapWithDtos.get(bookingShort.getItemId()).setLastBooking(bookingShort);
                };
        List<BookingShort> lastBookingsOfItems = bookingRepository.lastBookingsOfItems(mapWithDtos.keySet(),
                LocalDateTime.now());
        lastBookingsOfItems.forEach(setLastBooking);

        Consumer<BookingShort> setNextFutureBooking = bookingShort -> {
            mapWithDtos.get(bookingShort.getItemId()).setNextBooking(bookingShort);
        };
        List<BookingShort> nBookingsOfItems = bookingRepository.nearestFutureBookingsOfItems(mapWithDtos.keySet(),
                LocalDateTime.now());
        nBookingsOfItems.forEach(setNextFutureBooking);

        return new ArrayList<>(mapWithDtos.values());
    }

    public ItemDtoInFromServerBookingDto transformToNestedDto(Item item) {
        return new ItemDtoInFromServerBookingDto(item.getId(), item.getName());
    }

    public WithBookingsFromServerItemDto transformToWithEmptyBookingsDto(Item item) {
        List<Comment> comments = commentsRepository.findByItemId(item.getId());
        List<FromServerCommentDto> commentsDtos = commentDtoMapper.convertToDto(comments);
        return new WithBookingsFromServerItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                commentsDtos
        );
    }
}