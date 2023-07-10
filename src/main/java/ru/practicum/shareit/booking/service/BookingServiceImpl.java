package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.error.exception.BookingNotFoundException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.mapToBooking;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {
    ItemService itemService;
    UserService userService;
    BookingRepository repository;

    @Override
    public Booking getBookingById(long userId, long bookingId) {
        userService.getByUserId(userId);
        Booking  booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        User user = booking.getBooker();
        User owner = booking.getItem().getUser();
        if (userId == user.getId() || userId == owner.getId()) {
            log.info("[BookingService] -> found correct booking");
            return booking;
        }
        throw new NotFoundException(String.format("User with id =%d is not the owner", userId));
    }

    @Override
    public List<Booking> getByUserId(long userId, String state, int from, int size) throws ValidationException {
        userService.getByUserId(userId);
        if (from < 0) {
            throw new ValidationException("from parameter should be positive");
        }
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "start");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        switch (state) {
            case "ALL":
                return repository.getByBookerIdOrderByStartDesc(userId, page).toList();
            case "CURRENT":
                return repository.getCurrentByUserId(userId, page).toList();
            case "PAST":
                return repository.getBookingByUserIdAndFinishAfterNow(userId, page).toList();
            case "FUTURE":
                return repository.getBookingByUserIdAndStarBeforeNow(userId, page).toList();
            case "WAITING":
            case "REJECTED":
                return repository.getByBookerIdAndStatusContainingIgnoreCaseOrderByStartDesc(userId, state, page).toList();
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<Booking> getByOwnerId(long userId, String state, int from, int size) throws ValidationException {
        userService.getByUserId(userId);
        if (from < 0) {
            throw new ValidationException("from parameter should be positive");
        }
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "start");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        switch (state) {
            case "ALL":
                return repository.findByOwnerId(userId, page).toList();
            case "CURRENT":
                return repository.getCurrentByOwnerId(userId, page).toList();
            case "PAST":
                return repository.getPastByOwnerId(userId, page).toList();
            case "FUTURE":
                return repository.getBookingByOwnerIdAndStarBeforeNowOrderByStartDesc(userId, page).toList();
            case "WAITING":
            case "REJECTED":
                return repository.getBookingByOwnerIdAndByStatusContainingIgnoreCase(userId, state, page).toList();
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Transactional
    @Override
    public Booking approveBooking(long userId, long bookingId, boolean approve) throws ValidationException {
        userService.getByUserId(userId);
        Booking  booking = repository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (booking.getStatus().equals("APPROVED")) {
            throw new ValidationException(String.format("Booking with id =%d already approved", bookingId));
        }
        User owner = booking.getItem().getUser();
        if (userId != (owner.getId())) {
            throw new NotFoundException(String.format("User with id =%d is not the owner", userId));
        }
        if (approve) {
            booking.setStatus("APPROVED");
        } else {
            booking.setStatus("REJECTED");
        }
        repository.save(booking);
        return booking;
    }

    @Transactional
    @Override
    public Booking addNewBooking(long userId, BookingDto bookingDto) throws ValidationException {
        if (!bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            throw new ValidationException("Start time must be before end time");
        }
        User user = userService.getByUserId(userId);
        Item item = itemService.getItemById(bookingDto.getItemId());
        if (userId == item.getUser().getId()) {
            throw  new NotFoundException(String.format("User with ID =%d is not the owner", userId));
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available for booking");
        }
        Booking booking = mapToBooking(user, item, bookingDto);
        booking.setStatus("WAITING");
        repository.save(booking);
        return booking;
    }
}