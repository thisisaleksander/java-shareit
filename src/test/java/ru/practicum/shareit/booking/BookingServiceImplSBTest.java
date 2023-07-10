package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplSBTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    void approveBookingTest() throws ValidationException {
        User user = new User();
        user.setEmail("1@1.ru");
        user.setName("1");
        userService.save(user);
        User userFromBase = userService.getAll().get(0);
        User user2 = new User();
        user2.setEmail("12@2.ru");
        user2.setName("2");
        userService.save(user2);
        User userFromBase2 = userService.getAll().get(1);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("i1");
        itemDto.setAvailable(true);
        itemService.add(userFromBase.getId(), itemDto);
        List<ItemWithBooking> itemsFromBase = (List<ItemWithBooking>) itemService.getItemsBy(userFromBase.getId(), 1, 10);
        ItemWithBooking itemFromBase = itemsFromBase.get(0);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(itemFromBase.getId());
        bookingDto.setStart(LocalDateTime.of(2024, 7, 7, 7, 7));
        bookingDto.setEnd(LocalDateTime.of(2025, 7, 7, 7, 7));

        bookingService.addNewBooking(userFromBase2.getId(), bookingDto);
        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.booker = :booker", Booking.class);
        Booking bookingFromBase = query.setParameter("booker", userFromBase2).getSingleResult();

        assertThat(bookingFromBase.getStatus(), equalTo("WAITING"));


        bookingService.approveBooking(userFromBase.getId(), bookingFromBase.getId(), true);
        Booking bookingFromBaseApproved = query.setParameter("booker", userFromBase2).getSingleResult();

        assertThat(bookingFromBaseApproved.getStatus(), equalTo("APPROVED"));

    }
}