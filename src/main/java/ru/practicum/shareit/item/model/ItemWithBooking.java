package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.user.User;
import java.util.List;
import java.util.Objects;

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
    long  requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemWithBooking that = (ItemWithBooking) o;
        return id == that.id &&
                count == that.count &&
                requestId == that.requestId &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(user, that.user) &&
                Objects.equals(available, that.available) &&
                Objects.equals(lastBooking, that.lastBooking) &&
                Objects.equals(nextBooking, that.nextBooking) &&
                Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, count, user, available, lastBooking, nextBooking, comments, requestId);
    }
}
