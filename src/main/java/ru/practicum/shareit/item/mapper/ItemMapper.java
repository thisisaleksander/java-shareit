package ru.practicum.shareit.item.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.user.User;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class ItemMapper {

    public static Item mapToItem(User user, ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setCount(dto.getCount());
        item.setUser(user);
        item.setAvailable(dto.getAvailable());
        item.setRequestId(dto.getRequestId());
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getCount(),
                item.getAvailable(),
                item.getRequestId()
        );
    }

    public static ItemWithBooking mapToItemWithBooking(Item item, BookingDto lastBooking, BookingDto nextBooking, List<Comment> comments) {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        itemWithBooking.setId(item.getId());
        itemWithBooking.setName(item.getName());
        itemWithBooking.setDescription(item.getDescription());
        itemWithBooking.setCount(item.getCount());
        itemWithBooking.setUser(item.getUser());
        itemWithBooking.setAvailable(item.getAvailable());
        itemWithBooking.setLastBooking(lastBooking);
        itemWithBooking.setNextBooking(nextBooking);
        itemWithBooking.setComments(comments);
        itemWithBooking.setRequestId(item.getRequestId());
        return itemWithBooking;
    }

    public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
        List<ItemDto> allDto = new ArrayList<>();
        for (Item item : items) {
            allDto.add(mapToItemDto(item));
        }
        return allDto;
    }
}
