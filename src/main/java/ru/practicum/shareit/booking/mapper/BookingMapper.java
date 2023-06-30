package ru.practicum.shareit.booking.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@NoArgsConstructor
public final class BookingMapper {
    public static Booking toBooking(User user, Item item, BookingDto dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(dto.getEndTime());
        return booking;
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStartTime(),
                booking.getEndTime()
        );
    }
}