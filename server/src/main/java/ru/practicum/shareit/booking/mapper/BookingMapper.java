package ru.practicum.shareit.booking.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@NoArgsConstructor
public class BookingMapper {
    public static Booking mapToBooking(User user, Item item, BookingDto dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }
}