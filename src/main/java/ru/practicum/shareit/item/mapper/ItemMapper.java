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
public final class ItemMapper {

    public static Item toItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.isAvailable())
                .owner(user)
                .build();
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public static ItemWithBooking toItemWithBooking(Item item, BookingDto lastBooking, BookingDto nextBooking, List<Comment> comments) {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        itemWithBooking.setId(item.getId());
        itemWithBooking.setName(item.getName());
        itemWithBooking.setDescription(item.getDescription());
        itemWithBooking.setUser(item.getOwner());
        itemWithBooking.setIsAvailable(item.getAvailable());
        itemWithBooking.setLastBooking(lastBooking);
        itemWithBooking.setNextBooking(nextBooking);
        itemWithBooking.setComments(comments);
        return itemWithBooking;
    }

    public static List<ItemDto> toItemsDto(Iterable<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(toItemDto(item));
        }
        return dtos;
    }
}
