package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.error.exception.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
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
    public Booking getBookingById(Integer userId, Integer bookingId) throws ValidationException {
        userService.getBy(userId);
        Booking  booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.join(" ",
                        "[BookingServiceImpl] -> booking with id", bookingId.toString(), "not found")));
        User user = booking.getBooker();
        User owner = booking.getItem().getOwner();
        if (Objects.equals(userId, user.getId()) || Objects.equals(userId, owner.getId())) {
            log.info("[BookingServiceImpl] -> found booking with id {}", bookingId);
            return booking;
        }
        throw new ValidationException(String.join(" ",
                "[BookingServiceImpl] -> user is not then owner of booking with id", bookingId.toString()));
    }

    @Override
    public List<Booking> getByUserId(Integer userId, String status) throws ValidationException {
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
        throw new ValidationException(String.join(" ",
                "[BookingServiceImpl] -> unknown status:", status));
    }

    @Override
    public List<Booking> getByOwnerId(Integer userId, String status) throws ValidationException {
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
        throw new ValidationException(String.join(" ",
                "[BookingServiceImpl] -> unknown status:", status));
    }

    @Transactional
    @Override
    public Booking approve(Integer userId, Integer bookingId, Boolean approve) throws ValidationException {
        userService.getBy(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.join(" ",
                        "[BookingServiceImpl] -> booking with id", bookingId.toString(), "not found")));
        if (booking.getStatus().equals("APPROVED")) {
            throw new ValidationException(String.join(" ",
                    "[BookingServiceImpl] -> booking", bookingId.toString(), "already approved"));
        }
        User owner = booking.getItem().getOwner();
        if (!Objects.equals(userId, owner.getId())) {
            throw new ValidationException(String.join(" ",
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
    public Booking add(Integer userId, BookingDto bookingDto) throws ValidationException {
        if (!bookingDto.getStartTime().isBefore(bookingDto.getEndTime())) {
            throw new ValidationException("[BookingServiceImpl] -> booking is invalid");
        }
        User user = toUser(userService.getBy(userId));
        Item item = itemService.getByItemId(bookingDto.getItemId());
        if (Objects.equals(userId, item.getOwner().getId())) {
            throw new NotFoundException(String.join(" ",
                    "[BookingServiceImpl] -> user is not then owner of booking with id",
                    bookingDto.getId().toString()));
        }
        if (!item.getAvailable()) {
            throw new ValidationException(String.join(" ",
                    "[BookingServiceImpl] -> booking", bookingDto.getId().toString(), "is not available"));
        }
        Booking booking = toBooking(user, item, bookingDto);
        booking.setStatus("WAITING");
        bookingRepository.save(booking);
        log.info("[BookingServiceImpl] -> added new booking with id {}", booking.getId());
        return booking;
    }
}