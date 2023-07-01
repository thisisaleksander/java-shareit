package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.InvalidBookingException;
import ru.practicum.shareit.booking.exception.InvalidStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.exception.InvalidOwnerException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toBooking;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {
    ItemService itemService;
    UserService userService;
    BookingRepository bookingRepository;

    @Override
    public Booking getBookingById(Integer userId, Integer bookingId) {
        userService.getBy(userId);
        Booking  booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(String.join(" ",
                        "[BookingServiceImpl] -> booking with id", bookingId.toString(), "not found")));
        User user = booking.getBooker();
        User owner = booking.getItem().getOwner();
        if (Objects.equals(userId, user.getId()) || Objects.equals(userId, owner.getId())) {
            log.info("[BookingServiceImpl] -> found booking with id {}", bookingId);
            return booking;
        }
        throw new InvalidOwnerException(String.join(" ",
                "[BookingServiceImpl] -> user is not then owner of booking with id", bookingId.toString()));
    }

    @Override
    public List<Booking> getByUserId(Integer userId, String status) {
        userService.getBy(userId);
        switch (status) {
            case "ALL":
                return bookingRepository.findByUserId(userId);
            case "CURRENT":
                return bookingRepository.getCurrentByUserId(userId);
            case "PAST":
                return bookingRepository.getBookingByUserIdAndEndTimeAfterNow(userId);
            case "FUTURE":
                return bookingRepository.getBookingByUserIdAndStarTimeBeforeNow(userId);
            case "WAITING":
            case "REJECTED":
                return bookingRepository.getBookingByUserIdAndByStatusContainingIgnoreCase(userId, status);
        }
        throw new InvalidStatusException(String.join(" ",
                "[BookingServiceImpl] -> unknown status:", status));
    }

    @Override
    public List<Booking> getByOwnerId(Integer userId, String status) {
        userService.getBy(userId);
        switch (status) {
            case "ALL":
                return bookingRepository.findByOwner(userId);
            case "CURRENT":
                return bookingRepository.getCurrentByOwner(userId);
            case "PAST":
                return bookingRepository.getPastBookingByOwner(userId);
            case "FUTURE":
                return bookingRepository.getBookingByOwnerAndStarTimeBeforeNow(userId);
            case "WAITING":
            case "REJECTED":
                return bookingRepository.getBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, status);
        }
        throw new InvalidStatusException(String.join(" ",
                "[BookingServiceImpl] -> unknown status:", status));
    }

    @Transactional
    @Override
    public Booking approve(Integer userId, Integer bookingId, Boolean approve) {
        userService.getBy(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(String.join(" ",
                        "[BookingServiceImpl] -> booking with id", bookingId.toString(), "not found")));
        if (booking.getStatus().equals("APPROVED")) {
            throw new InvalidStatusException(String.join(" ",
                    "[BookingServiceImpl] -> booking", bookingId.toString(), "already approved"));
        }
        User owner = booking.getItem().getOwner();
        if (!Objects.equals(userId, owner.getId())) {
            throw new InvalidOwnerException(String.join(" ",
                    "[BookingServiceImpl] -> user is not then owner of booking with id", bookingId.toString()));
        }
        if (approve) {
            booking.setStatus("APPROVED");
        } else {
            booking.setStatus("REJECTED");
        }
        bookingRepository.save(booking);
        log.info("[BookingServiceImpl] -> booking {} approved", bookingId);
        return booking;
    }

    @Transactional
    @Override
    public Booking add(Integer userId, BookingDto bookingDto) {
        if (!bookingDto.getStartTime().isBefore(bookingDto.getEndTime())) {
            throw new InvalidBookingException("[BookingServiceImpl] -> booking is invalid");
        }
        User user = toUser(userService.getBy(userId));
        Item item = itemService.getByItemId(bookingDto.getItemId());
        if (Objects.equals(userId, item.getOwner().getId())) {
            throw new InvalidOwnerException(String.join(" ",
                    "[BookingServiceImpl] -> user is not then owner of booking with id",
                    bookingDto.getId().toString()));
        }
        if (!item.getAvailable()) {
            throw new InvalidStatusException(String.join(" ",
                    "[BookingServiceImpl] -> booking", bookingDto.getId().toString(), "is not available"));
        }
        Booking booking = toBooking(user, item, bookingDto);
        booking.setStatus("WAITING");
        bookingRepository.save(booking);
        log.info("[BookingServiceImpl] -> added new booking with id {}", booking.getId());
        return booking;
    }
}