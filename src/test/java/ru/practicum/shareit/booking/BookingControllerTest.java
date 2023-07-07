package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.mapper.BookingMapper.mapToBookingDto;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    BookingService bookingService;
    @InjectMocks
    BookingController bookingController;

    @Test
    void getByIdTest() {
        Booking booking = new Booking();
        when(bookingService.getBookingById(1L, 1L)).thenReturn(booking);

        Booking response = bookingController.getById(1L, 1L);

        assertEquals(booking, response);
    }

    @Test
    void getByUserIdTest() throws ValidationException {
        List<Booking> bookings = List.of(new Booking());
        when(bookingService.getByUserId(1L, "ALL",1, 10)).thenReturn(bookings);

        List<Booking> response = bookingController.getByUserId(1L, "ALL",1, 10);

        assertEquals(bookings, response);
    }

    @Test
    void getByOwnerIdTest() throws ValidationException {
        List<Booking> bookings = List.of(new Booking());
        when(bookingService.getByOwnerId(1L, "ALL",1, 10)).thenReturn(bookings);

        List<Booking> response = bookingController.getByOwnerId(1L, "ALL",1, 10);

        assertEquals(bookings, response);
    }

    @Test
    void approveBookingTest() throws ValidationException {
        Booking booking = new Booking();
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        Booking response = bookingController.approveBooking(1L, 1L, false);

        assertEquals(booking, response);
    }

    @Test
    void addTest() throws ValidationException {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        booking.setItem(item);
        booking.setBooker(user);
        when(bookingService.addNewBooking(1L, mapToBookingDto(booking))).thenReturn(booking);

        Booking response = bookingController.add(1L, mapToBookingDto(booking));

        assertEquals(booking, response);
    }
}