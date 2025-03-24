package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestFromServerDto convertToFromServerDto(ItemRequest itemRequest) {
        ItemRequestFromServerDto result = new ItemRequestFromServerDto();
        result.setId(itemRequest.getId());
        result.setCreatorId(itemRequest.getCreatorId());
        result.setDescription(itemRequest.getDescription());
        result.setCreated(itemRequest.getCreated());
        result.setItems(itemRequest.getItems().stream().map(ItemRequestMapper::transformItem).toList());
        return result;
    }

    public static List<ItemRequestFromServerDto> convertToFromServerDtoList(List<ItemRequest> itemRequestsList) {
        return itemRequestsList.stream().map(ItemRequestMapper::convertToFromServerDto).toList();
    }

    private static ItemInRequestDto transformItem(Item item) {
        ItemInRequestDto result = new ItemInRequestDto();
        result.setId(item.getId());
        result.setOwnerId(item.getOwner().getId());
        result.setName(item.getName());
        return result;
    }
}
