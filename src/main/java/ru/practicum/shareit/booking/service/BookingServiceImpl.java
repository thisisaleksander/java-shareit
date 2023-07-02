package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toEntity;

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
    public Booking getBookingById(long userId, long bookingId) {
        userService.getByUserId(userId);
        Booking  booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("[BookingService] -> Booking with id =%d not found", bookingId)));
        User user = booking.getBooker();
        User owner = booking.getItem().getUser();
        if (userId == user.getId() || userId == owner.getId()) {
            log.info("[BookingService] -> found correct booking");
            return booking;
        }
        throw new NotFoundException(String.format("[BookingService] -> User with id =%d is not the owner", userId));
    }

    @Override
    public List<Booking> getByUserId(long userId, String state) throws ValidationException {
        userService.getByUserId(userId);
        switch (state) {
            case "ALL":
                return bookingRepository.findByUserId(userId);
            case "CURRENT":
                return bookingRepository.getCurrentByUserId(userId);
            case "PAST":
                return bookingRepository.getBookingByUserIdAndFinishAfterNow(userId);
            case "FUTURE":
                return bookingRepository.getBookingByUserIdAndStarBeforeNow(userId);
            case "WAITING":
            case "REJECTED":
                return bookingRepository.getBookingByUserIdAndByStatusContainingIgnoreCase(userId, state);
        }
        throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Override
    public List<Booking> getByOwnerId(long userId, String state) throws ValidationException {
        userService.getByUserId(userId);
        switch (state) {
            case "ALL":
                return bookingRepository.findByOwnerId(userId);
            case "CURRENT":
                return bookingRepository.getCurrentByOwnerId(userId);
            case "PAST":
                return bookingRepository.getPastByOwnerId(userId);
            case "FUTURE":
                return bookingRepository.getBookingByOwnerIdAndStarBeforeNow(userId);
            case "WAITING":
            case "REJECTED":
                return bookingRepository.getBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, state);
        }
        throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
    }

    @Transactional
    @Override
    public Booking approveBooking(long userId, long bookingId, boolean approve) throws ValidationException {
        userService.getByUserId(userId);
        Booking  booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException(String.format("Booking with id =%d not found", bookingId)));
        if (booking.getStatus().equals("APPROVED")) {
            throw new ValidationException(String.format("[BookingService] -> Booking with id =%d already approved", bookingId));
        }
        User owner = booking.getItem().getUser();
        if (userId != (owner.getId())) {
            throw new NotFoundException(String.format("[BookingService] -> User with id =%d is not the owner", userId));
        }
        if (approve) {
            booking.setStatus("APPROVED");
        } else {
            booking.setStatus("REJECTED");
        }
        bookingRepository.save(booking);
        return booking;
    }

    @Transactional
    @Override
    public Booking addNewBooking(long userId, BookingDto bookingDto) throws ValidationException {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new ValidationException("[BookingService] -> start time must be before end time");
        }
        User user = userService.getByUserId(userId);
        Item item = itemService.getItemById(bookingDto.getItemId());
        if (userId == item.getUser().getId()) {
            throw  new NotFoundException(String.format("[BookingService] -> user with ID =%d is not the owner", userId));
        }
        if (!item.getAvailable()) {
            throw new ValidationException("[BookingService] -> Item is not available for booking");
        }
        Booking booking = toEntity(user, item, bookingDto);
        booking.setStatus("WAITING");
        bookingRepository.save(booking);
        return booking;
    }
}