package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.mapper.BookingMapper.mapToBookingDto;

@WebMvcTest
class BookingControllerWebMvcTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void getByIdTest() {
        Booking booking = new Booking();
        when(bookingService.getBookingById(1L, 1L)).thenReturn(booking);

        String response = mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(booking), response);
    }

    @SneakyThrows
    @Test
    void getByUserIdTest() {
        List<Booking> bookings = List.of(new Booking());
        when(bookingService.getByUserId(1L, "ALL",1, 10)).thenReturn(bookings);

        String response = mockMvc.perform(get("/bookings", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookings), response);
    }

    @SneakyThrows
    @Test
    void getByOwnerIdTest() {
        List<Booking> bookings = List.of(new Booking());
        when(bookingService.getByOwnerId(1L, "ALL",1, 10)).thenReturn(bookings);

        String response = mockMvc.perform(get("/bookings/owner", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookings), response);
    }

    @SneakyThrows
    @Test
    void approveBookingTest() {
        Booking booking = new Booking();
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        String response = mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(booking), response);
    }

    @SneakyThrows
    @Test
    void addTestShouldThrowExceptionStart() {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.of(2020, 07, 07, 07, 07));
        booking.setEnd(LocalDateTime.of(2023, 07, 8, 07, 07));
        booking.setId(1L);
        when(bookingService.addNewBooking(1L, mapToBookingDto(booking))).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mapToBookingDto(booking))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, never()).addNewBooking(anyLong(), any(BookingDto.class));

    }

    @SneakyThrows
    @Test
    void addTestShouldThrowExceptionEnd() {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.of(2022, 07, 07, 07, 07));
        booking.setEnd(LocalDateTime.of(2020, 07, 8, 07, 07));
        booking.setId(1L);
        when(bookingService.addNewBooking(1L, mapToBookingDto(booking))).thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mapToBookingDto(booking))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, never()).addNewBooking(anyLong(), any(BookingDto.class));

    }
}