package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.mapper.BookingMapper.mapToBooking;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    ItemService itemService;
    @Mock
    UserService userService;
    @Mock
    BookingRepository bookingRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void getBookingByIdTest() {
        User user = new User();
        user.setId(1L);
        Booking booking = new Booking();
        booking.toString();
        booking.hashCode();
        booking.setBooker(user);
        Item item = new Item();
        User owner = new User();
        item.setUser(owner);
        booking.setItem(item);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking actualBooking = bookingService.getBookingById(1L, 1L);

        assertEquals(booking, actualBooking);
    }

    @Test
    void getBookingByIdTestShouldThrowException() {
        User user = new User();
        user.setId(2L);
        Booking booking = new Booking();
        booking.setBooker(user);
        Item item = new Item();
        User owner = new User();
        item.setUser(owner);
        booking.setItem(item);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void getByUserIdTestAndThrowExceptionIfStateIsWrong() throws ValidationException {
        Booking booking = new Booking();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        int from = 1;
        int size = 10;
        int pageIndex = from / size;
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        Page<Booking> bookingPage = new PageImpl<>(bookings, page, bookings.size());
        when(bookingRepository.getByBookerIdOrderByStartDesc(anyLong(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.getCurrentByUserId(anyLong(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.getBookingByUserIdAndFinishAfterNow(anyLong(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.getBookingByUserIdAndStarBeforeNow(anyLong(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.getByBookerIdAndStatusContainingIgnoreCaseOrderByStartDesc(anyLong(), anyString(), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> actualBookingList = bookingService.getByUserId(1L, "ALL", 1, 10);
        assertEquals(bookings, actualBookingList);
        List<Booking> actualBookingListCur = bookingService.getByUserId(1L, "CURRENT", 1, 10);
        assertEquals(bookings, actualBookingListCur);
        List<Booking> actualBookingListPast = bookingService.getByUserId(1L, "PAST", 1, 10);
        assertEquals(bookings, actualBookingListPast);
        List<Booking> actualBookingListFuture = bookingService.getByUserId(1L, "FUTURE", 1, 10);
        assertEquals(bookings, actualBookingListFuture);
        List<Booking> actualBookingListWaiting = bookingService.getByUserId(1L, "WAITING", 1, 10);
        assertEquals(bookings, actualBookingListWaiting);
        assertThrows(ValidationException.class,
                () -> bookingService.getByUserId(1L, "WRONG", 1, 10));
    }

    @Test
    void getByOwnerIdTestAndThrowExceptionIfStateIsWrong() throws ValidationException {
        Booking booking = new Booking();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        int from = 1;
        int size = 10;
        int pageIndex = from / size;
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        Page<Booking> bookingPage = new PageImpl<>(bookings, page, bookings.size());
        when(bookingRepository.findByOwnerId(anyLong(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.getCurrentByOwnerId(anyLong(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.getPastByOwnerId(anyLong(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.getBookingByOwnerIdAndStarBeforeNowOrderByStartDesc(anyLong(), any(Pageable.class))).thenReturn(bookingPage);
        when(bookingRepository.getBookingByOwnerIdAndByStatusContainingIgnoreCase(anyLong(), anyString(), any(Pageable.class))).thenReturn(bookingPage);

        List<Booking> actualBookingList = bookingService.getByOwnerId(1L, "ALL", 1, 10);
        assertEquals(bookings, actualBookingList);
        List<Booking> actualBookingListCur = bookingService.getByOwnerId(1L, "CURRENT", 1, 10);
        assertEquals(bookings, actualBookingListCur);
        List<Booking> actualBookingListPast = bookingService.getByOwnerId(1L, "PAST", 1, 10);
        assertEquals(bookings, actualBookingListPast);
        List<Booking> actualBookingListFuture = bookingService.getByOwnerId(1L, "FUTURE", 1, 10);
        assertEquals(bookings, actualBookingListFuture);
        List<Booking> actualBookingListWaiting = bookingService.getByOwnerId(1L, "WAITING", 1, 10);
        assertEquals(bookings, actualBookingListWaiting);
        assertThrows(ValidationException.class,
                () -> bookingService.getByOwnerId(1L, "WRONG", 1, 10));
    }

    @Test
    void approveBookingTestApproved() throws ValidationException {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        user.setId(1L);
        item.setUser(user);
        booking.setStatus("WAITING");
        booking.setItem(item);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking actualBooking = bookingService.approveBooking(1L, 1L, true);

        assertEquals(booking, actualBooking);

        assertEquals("APPROVED", actualBooking.getStatus());

        verify(bookingRepository).save(booking);
    }

    @Test
    void approveBookingTestRejected() throws ValidationException {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        user.setId(1L);
        item.setUser(user);
        booking.setStatus("WAITING");
        booking.setItem(item);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        Booking actualBooking = bookingService.approveBooking(1L, 1L, false);

        assertEquals(booking, actualBooking);

        assertEquals("REJECTED", actualBooking.getStatus());

        verify(bookingRepository).save(booking);
    }

    @Test
    void approveBookingTestShouldThrowNotFoundException() {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        user.setId(1L);
        item.setUser(user);
        booking.setStatus("WAITING");
        booking.setItem(item);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(2L, 1L, true));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveBookingTestShouldThrowValidationException() {
        Booking booking = new Booking();
        Item item = new Item();
        User user = new User();
        user.setId(1L);
        item.setUser(user);
        booking.setStatus("APPROVED");
        booking.setItem(item);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(1L, 1L, true));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void addNewBookingTest() throws ValidationException {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2023, 07, 07, 07, 07));
        bookingDto.setEnd(LocalDateTime.of(2023, 07, 8, 07, 07));
        User user = new User();
        user.setId(1L);
        User owner = new User();
        owner.setId(2L);
        Item item = new Item();
        item.setId(1L);
        item.setUser(owner);
        item.setAvailable(true);
        bookingDto.setItemId(1L);
        Booking booking = mapToBooking(user, item, bookingDto);
        booking.setStatus("WAITING");
        when(userService.getByUserId(1L)).thenReturn(user);
        when(itemService.getItemById(1L)).thenReturn(item);

        Booking actualBooking = bookingService.addNewBooking(1L, bookingDto);

        assertEquals(booking, actualBooking);
        verify(bookingRepository).save(booking);
    }

    @Test
    void addNewBookingTestShouldThrowValidationException() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2023, 07, 07, 07, 07));
        bookingDto.setEnd(LocalDateTime.of(2023, 06, 8, 07, 07));
        User user = new User();
        user.setId(1L);
        User owner = new User();
        owner.setId(2L);
        Item item = new Item();
        item.setId(1L);
        item.setUser(owner);
        item.setAvailable(true);
        bookingDto.setItemId(1L);
        Booking booking = mapToBooking(user, item, bookingDto);
        booking.setStatus("WAITING");

        assertThrows(ValidationException.class,
                () -> bookingService.addNewBooking(1L, bookingDto));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void addNewBookingTestShouldThrowNotFoundException() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2023, 07, 07, 07, 07));
        bookingDto.setEnd(LocalDateTime.of(2023, 07, 8, 07, 07));
        User user = new User();
        user.setId(1L);
        User owner = new User();
        owner.setId(2L);
        Item item = new Item();
        item.setId(1L);
        item.setUser(user);
        item.setAvailable(true);
        bookingDto.setItemId(1L);
        Booking booking = mapToBooking(user, item, bookingDto);
        booking.setStatus("WAITING");
        when(userService.getByUserId(1L)).thenReturn(user);
        when(itemService.getItemById(1L)).thenReturn(item);

        assertThrows(NotFoundException.class,
                () -> bookingService.addNewBooking(1L, bookingDto));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void addNewBookingTestShouldThrowValidationExceptionItemIsNotAvailable() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2023, 07, 07, 07, 07));
        bookingDto.setEnd(LocalDateTime.of(2023, 07, 8, 07, 07));
        User user = new User();
        user.setId(1L);
        User owner = new User();
        owner.setId(2L);
        Item item = new Item();
        item.setId(1L);
        item.setUser(owner);
        item.setAvailable(false);
        bookingDto.setItemId(1L);
        Booking booking = mapToBooking(user, item, bookingDto);
        booking.setStatus("WAITING");
        when(userService.getByUserId(1L)).thenReturn(user);
        when(itemService.getItemById(1L)).thenReturn(item);

        assertThrows(ValidationException.class,
                () -> bookingService.addNewBooking(1L, bookingDto));

        verify(bookingRepository, never()).save(any(Booking.class));
    }
}