package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.user.User;
import java.util.List;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemWithBooking {
    long id;
    String name;
    String description;
    long count;
    User user;
    Boolean available;
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<Comment> comments;
}
