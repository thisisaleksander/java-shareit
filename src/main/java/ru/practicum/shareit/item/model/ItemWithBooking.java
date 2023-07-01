package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.user.User;
import java.util.List;


@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemWithBooking {
    Integer id;
    String name;
    String description;
    User user;
    Boolean available;
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<Comment> comments;
}
