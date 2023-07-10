package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemRepository;
import ru.practicum.shareit.request.service.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    int from = 1;
    int size = 10;
    int pageIndex = from / size;
    Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
    Pageable page = PageRequest.of(pageIndex, size, sortByDate);

    @BeforeEach
    private void addBookings() {
        Booking booking1 = new Booking();
        User user1 = new User();
        user1.setName("1");
        user1.setEmail("1@1.ru");
        user1.setId(1L);
        userRepository.save(user1);
        booking1.setBooker(user1);
        Item item1 = new Item();
        item1.setId(1L);
        item1.setDescription("D1");
        item1.setAvailable(true);
        item1.setUser(user1);
        item1.setName("I1");
        itemRepository.save(item1);
        booking1.setItem(item1);
        booking1.setStatus("WAITING");
        booking1.setStart(LocalDateTime.of(2021, 07, 07, 07, 07));
        booking1.setEnd(LocalDateTime.of(2022, 8, 07, 07, 07));
        bookingRepository.save(booking1);
        Booking booking2 = new Booking();
        User user2 = new User();
        user2.setName("2");
        user2.setEmail("2@2.ru");
        user2.setId(2L);
        userRepository.save(user2);
        booking2.setBooker(user2);
        Item item2 = new Item();
        item2.setId(2L);
        item2.setDescription("D2");
        item2.setAvailable(true);
        item2.setUser(user2);
        item2.setName("I2");
        itemRepository.save(item2);
        booking2.setItem(item2);
        booking2.setStatus("WAITING");
        booking2.setStart(LocalDateTime.of(2023, 01, 07, 07, 07));
        booking2.setEnd(LocalDateTime.of(2024, 01, 07, 07, 07));
        bookingRepository.save(booking2);
        Booking booking3 = new Booking();
        User user3 = new User();
        user3.setName("3");
        user3.setEmail("3@3.ru");
        user3.setId(3L);
        userRepository.save(user3);
        booking3.setBooker(user3);
        Item item3 = new Item();
        item3.setId(3L);
        item3.setDescription("D3");
        item3.setAvailable(true);
        item3.setUser(user3);
        item3.setName("I3");
        itemRepository.save(item3);
        booking3.setItem(item3);
        booking3.setStatus("WAITING");
        booking3.setStart(LocalDateTime.of(2024, 01, 07, 07, 07));
        booking3.setEnd(LocalDateTime.of(2025, 01, 07, 07, 07));
        bookingRepository.save(booking3);
    }

    @Test
    void findByIdTest() {
    }

    @Test
    void findFirstByItemIdAndStartBeforeAndStatusOrderByStartDescTest() {
    }

    @Test
    void findFirstByItemIdAndEndAfterAndStatusOrderByStartAscTest() {
    }

    @Test
    void getByBookerIdOrderByStartDescTest() {
    }

    @Test
    void getByBookerIdAndStatusContainingIgnoreCaseOrderByStartDescTest() {
    }

    @Test
    void getCurrentByUserIdTest() {
        List<Booking> bookings = bookingRepository.getCurrentByUserId(2L, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.get(0).getEnd().isAfter(LocalDateTime.now()));
    }

    @Test
    void getBookingByUserIdAndFinishAfterNowTest() {
        List<Booking> bookings = bookingRepository.getBookingByUserIdAndFinishAfterNow(1L, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getEnd().isBefore(LocalDateTime.now()));
    }

    @Test
    void getBookingByUserIdAndStarBeforeNowTest() {
        List<Booking> bookings = bookingRepository.getBookingByUserIdAndStarBeforeNow(3L, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getStart().isAfter(LocalDateTime.now()));
    }

    @Test
    void findByOwnerIdTest() {
        List<Booking> bookings = bookingRepository.findByOwnerId(3L, page).toList();

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.get(0).getItem().getId(), 3L);
    }

    @Test
    void getBookingByOwnerIdAndByStatusContainingIgnoreCaseTest() {
        List<Booking> bookings = bookingRepository.getBookingByOwnerIdAndByStatusContainingIgnoreCase(3L, "WAITING", page).toList();

        assertFalse(bookings.isEmpty());
        assertEquals(bookings.get(0).getItem().getId(), 3L);
    }

    @Test
    void getCurrentByOwnerIdTest() {
        List<Booking> bookings = bookingRepository.getCurrentByUserId(2L, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getStart().isBefore(LocalDateTime.now()));
        assertTrue(bookings.get(0).getEnd().isAfter(LocalDateTime.now()));
    }

    @Test
    void getPastByOwnerIdTest() {
        List<Booking> bookings = bookingRepository.getPastByOwnerId(1L, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getEnd().isBefore(LocalDateTime.now()));
    }

    @Test
    void getBookingByOwnerIdAndStarBeforeNowOrderByStartDescTest() {
        List<Booking> bookings = bookingRepository.getBookingByUserIdAndStarBeforeNow(3L, page).toList();

        assertFalse(bookings.isEmpty());
        assertTrue(bookings.get(0).getStart().isAfter(LocalDateTime.now()));
    }

    @Test
    void getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDescTest() {
    }
}