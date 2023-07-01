package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.service.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.InvalidOwnerException;
import ru.practicum.shareit.item.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.exception.ItemNotFountException;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;
import static ru.practicum.shareit.booking.mapper.BookingMapper.toBookingDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;

    private void checkIfOwnerIsCorrect(Integer userId, Item item) {
        if (!Objects.equals(userId, item.getOwner().getId())) {
            log.info("[ItemServiceImpl] -> user {} is not the owner of item {}", userId, item.getId());
            throw new InvalidOwnerException(String.join(" ",
                    "[ItemServiceImpl] -> user", userId.toString(), "is not the owner of this item"));
        }
    }

    private void checkIfUserExists(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.join(" ",
                        "[ItemServiceImpl] -> user with id", userId.toString(), "not found")));
    }

    @Override
    @Transactional
    public Item add(Integer userId, ItemDto itemDto) {
        if (itemDto.getAvailable() == null || !itemDto.getAvailable()) {
            log.warn("[ItemServiceImpl] -> item is not available");
            throw new ItemIsNotAvailableException("[ItemServiceImpl] -> item is not available");
        }
        if (nonNull(itemDto.getDescription())) {
            if (itemDto.getDescription().isEmpty()) {
                log.warn("[ItemServiceImpl] -> item is not available");
                throw new ItemIsNotAvailableException("[ItemServiceImpl] -> item is not available");
            }
        } else {
            log.warn("[ItemServiceImpl] -> item is not available");
            throw new ItemIsNotAvailableException("[ItemServiceImpl] -> item is not available");
        }
        if (nonNull(itemDto.getName())) {
            if (itemDto.getName().isEmpty()) {
                log.warn("[ItemServiceImpl] -> item is not available");
                throw new ItemIsNotAvailableException("[ItemServiceImpl] -> item is not available");
            }
        } else {
            log.warn("[ItemServiceImpl] -> item is not available");
            throw new ItemIsNotAvailableException("[ItemServiceImpl] -> item is not available");
        }
        User user = userRepository.findById(userId)
                 .orElseThrow(() -> new UserNotFoundException(String.join(" ",
                        "[ItemServiceImpl] -> user with id", userId.toString(), "not found")));
        Item item = toItem(itemDto, user);
        log.info("[ItemServiceImpl] -> saving new item");
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public ItemDto update(Integer userId, ItemDto itemDto, Integer itemId) {
        checkIfUserExists(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new UserNotFoundException(String.join(" ",
                        "[ItemServiceImpl] -> item with id", userId.toString(), "not found")));
        if (nonNull(itemDto.getName())) {
            item.setName(itemDto.getName());
            log.info("[ItemServiceImpl] -> item {} name updated", itemId);
        }
        if (nonNull(itemDto.getDescription())) {
            item.setDescription(itemDto.getDescription());
            log.info("[ItemServiceImpl] -> item {} description updated", itemId);
        }
        if (nonNull(itemDto.getAvailable())) {
            item.setAvailable(itemDto.getAvailable());
            log.info("[ItemServiceImpl] -> item {} availability updated", itemId);
        }
        itemRepository.save(item);
        log.info("[ItemServiceImpl] -> item {} data updated", itemId);
        return toItemDto(item);
    }

    @Override
    public List<ItemWithBooking> getByUserId(Integer userId) {
        checkIfUserExists(userId);
        return mapToItemWithBooking(itemRepository.findByOwnerId(userId));
    }

    @Override
    public ItemWithBooking getByItemId(Integer userId, Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFountException(String.join(" ",
                        "[ItemServiceImpl] -> item with id", itemId.toString(), "not found")));
        BookingDto lastBooking;
        BookingDto nextBooking;
        if (Objects.equals(userId, item.getOwner().getId())) {
            Booking lastBookingForDto = bookingRepository
                    .findFirstByItemIdAndStartTimeBeforeAndStatusOrderByStartTimeDesc(item.getId(),
                            LocalDateTime.now(),
                            "APPROVED")
                    .orElse(null);
            if (lastBookingForDto == null) {
                lastBooking = null;
            } else {
                lastBooking = toBookingDto(lastBookingForDto);
            }
            Booking nextBookingForDto = bookingRepository
                    .findFirstByItemIdAndEndTimeAfterAndStatusOrderByStartTimeAsc(item.getId(),
                            LocalDateTime.now(),
                            "APPROVED")
                    .orElse(null);
            if (nextBookingForDto != null && lastBookingForDto != null) {
                if (lastBookingForDto.equals(nextBookingForDto)) {
                    nextBookingForDto = null;
                }
            }
            if (nextBookingForDto == null) {
                nextBooking = null;
            } else {
                nextBooking = toBookingDto(nextBookingForDto);
            }
        } else {
            lastBooking = null;
            nextBooking = null;
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return toItemWithBooking(item, lastBooking, nextBooking, comments);
    }

    @Override
    public Item getByItemId(Integer itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFountException(String.join(" ",
                        "[ItemServiceImpl] -> item with id", itemId.toString(), "not found")));
    }

    @Override
    public List<ItemDto> findByText(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> userItems = itemRepository
                .getItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text);
        for (int i = 0; i < userItems.size(); i++) {
            if (!userItems.get(i).getAvailable()) userItems.remove(i);
        }
        log.info("[ItemServiceImpl] -> total items found: {}", userItems.size());
        return toItemsDto(userItems);
    }

    @Override
    @Transactional
    public void delete(Integer userId, Integer itemId) {
        checkIfUserExists(userId);
        checkIfOwnerIsCorrect(userId,
                itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFountException(String.join(" ",
                        "[ItemServiceImpl] -> item with id", itemId.toString(), "not found"))));
        log.info("[ItemServiceImpl] -> deleting item with id {}", itemId);
        itemRepository.deleteByOwnerIdAndId(userId, itemId);
    }

    @Override
    public Comment comment(Integer userId, Comment comment, Integer itemId) {
        log.info("[ItemServiceImpl] -> new comment fot item with id {}", itemId);
        List<Booking> bookings = bookingRepository.getBookingByUserIdAndEndTimeAfterNow(userId);
        boolean userIsBooker = false;
        for (Booking booking: bookings) {
            if (Objects.equals(booking.getItem().getId(), itemId)) {
                userIsBooker = true;
                break;
            }
        }
        if (userIsBooker) {
            comment.setItem(itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFountException(String.join(" ",
                    "[ItemServiceImpl] -> item with id", itemId.toString(), "not found"))));
            comment.setAuthor(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.join(" ",
                    "[ItemServiceImpl] -> user with id", userId.toString(), "not found"))));
            comment.setCreated(LocalDateTime.now());
            return commentRepository.save(comment);
        } else throw new InvalidOwnerException(String.join(" ",
                "[ItemServiceImpl] -> owner of item with id", itemId.toString(), "not valid"));
    }

    public List<ItemWithBooking> mapToItemWithBooking(Iterable<Item> items) {
        List<ItemWithBooking> itemWithBooking = new ArrayList<>();
        for (Item item : items) {
            Booking lastBookingForDto = bookingRepository.findFirstByItemIdAndStartTimeBeforeAndStatusOrderByStartTimeDesc(
                            item.getId(),
                            LocalDateTime.now(),
                            "APPROVED")
                    .orElse(null);
            BookingDto lastBooking;
            if (lastBookingForDto == null) {
                lastBooking = null;
            } else {
                lastBooking = toBookingDto(lastBookingForDto);
            }
            Booking nextBookingForDto = bookingRepository.findFirstByItemIdAndEndTimeAfterAndStatusOrderByStartTimeAsc(
                            item.getId(),
                            LocalDateTime.now(),
                            "APPROVED")
                    .orElse(null);
            BookingDto nextBooking;
            if (nextBookingForDto == null) {
                nextBooking = null;
            } else {
                nextBooking = toBookingDto(nextBookingForDto);
            }
            List<Comment> comments = commentRepository.findAllByItemId(item.getId());
            itemWithBooking.add(toItemWithBooking(item, lastBooking, nextBooking, comments));
        }
        return itemWithBooking;
    }
}