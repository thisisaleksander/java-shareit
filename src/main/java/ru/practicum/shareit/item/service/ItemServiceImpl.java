package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.service.CommentRepository;
import ru.practicum.shareit.error.exception.ItemNotFoundException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.UserNotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserRepository;
import ru.practicum.shareit.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.toDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.mapToItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.mapToItemDto;

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

    DateUtils dateUtils = new DateUtils();

    @Override
    public Collection<ItemWithBooking> getItemsBy(long userId) {
        checkExistUserById(userId);
        return mapToItemWithBooking(itemRepository.findByUserId(userId));
    }

    @Override
    public ItemWithBooking getItemById(long userId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        BookingDto lastBooking;
        BookingDto nextBooking;
        if (userId == item.getUser().getId()) {
            Booking lastBookingForDto = bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(),
                            dateUtils.now(),
                            "APPROVED")
                    .orElse(null);
            if (lastBookingForDto == null) {
                lastBooking = null;
            } else {
                lastBooking = toDto(lastBookingForDto);
            }
            Booking nextBookingForDto = bookingRepository.findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(
                            item.getId(),
                            dateUtils.now(),
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
                nextBooking = toDto(nextBookingForDto);
            }
        } else {
            lastBooking = null;
            nextBooking = null;
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return ItemMapper.mapToItemWithBooking(item, lastBooking, nextBooking, comments);
    }

    @Override
    public Item getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Transactional
    @Override
    public ItemDto update(long userId, ItemDto itemDto, long itemId) {
        checkExistUserById(userId);
        Item item = itemRepository.findById(itemId).get();
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(item);
        log.info("[ItemService] -> item with id {} updated", itemId);
        return mapToItemDto(item);
    }

    @Transactional
    @Override
    public Item add(long userId, ItemDto itemDto) throws ValidationException {

        if (itemDto.getAvailable() == null || !itemDto.getAvailable()) {
            throw new ValidationException("Item is not available");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Item item = mapToItem(user, itemDto);
        checkExistUserById(userId);
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public void delete(long userId, long itemId) {
        log.info("[ItemService] -> deleting item with id {}", itemId);
        checkExistUserById(userId);
        validateOwner(userId, itemRepository.findById(itemId).get());
        itemRepository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    public Comment addComment(long userId, Comment comment, long itemId) throws ValidationException {
        List<Booking> bookings = bookingRepository.getBookingByUserIdAndFinishAfterNow(userId);
        boolean userIsBooker = false;
        for (Booking booking: bookings) {
            if (booking.getItem().getId() == itemId) {
                userIsBooker = true;
                break;
            }
        }
        if (userIsBooker) {
            comment.setItem(itemRepository.findById(itemId).orElseThrow(
                    () -> new ItemNotFoundException(itemId))
            );
            comment.setAuthorName(userRepository.findById(userId).orElseThrow(
                    () -> new UserNotFoundException(userId)).getName()
            );
            comment.setCreated(dateUtils.now());
            return commentRepository.save(comment);
        } else throw new ValidationException("User did not book this item");
    }

    @Override
    public List<ItemDto> findByText(String query) {
        log.info("[ItemService] -> trying to find items by {}", query);
        if (query.isBlank()) {
            log.warn("[ItemService] -> query from request is blank");
            return new ArrayList<>();
        }
        List<Item> userItems = itemRepository.getItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        for (int i = 0; i < userItems.size(); i++) {
            if (!userItems.get(i).getAvailable()) {
                userItems.remove(i);
            }
        }
        log.info("[ItemService] -> total found: {}", userItems.size());
        return mapToItemDto(userItems);
    }

    private void validateOwner(long userId, Item item) {
        if (userId != item.getUser().getId()) {
            log.warn("[ItemService] -> user id is incorrect");
            throw new NotFoundException("User is not the owner");
        }
    }

    private void checkExistUserById(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<ItemWithBooking> mapToItemWithBooking(Iterable<Item> items) {
        List<ItemWithBooking> itemWithBookings = new ArrayList<>();
        for (Item item : items) {
            Booking lastBookingForDto = bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
                    item.getId(),
                    dateUtils.now(),
                    "APPROVED")
                    .orElse(null);
            BookingDto lastBooking;
            if (lastBookingForDto == null) {
                lastBooking = null;
            } else {
                lastBooking = toDto(lastBookingForDto);
            }
            Booking nextBookingForDto = bookingRepository.findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(
                    item.getId(),
                    dateUtils.now(),
                    "APPROVED")
                    .orElse(null);
            BookingDto nextBooking;
            if (nextBookingForDto == null) {
                nextBooking = null;
            } else {
                nextBooking = toDto(nextBookingForDto);
            }
            List<Comment> comments = commentRepository.findAllByItemId(item.getId());
            itemWithBookings.add(ItemMapper.mapToItemWithBooking(item, lastBooking, nextBooking, comments));
        }
        return itemWithBookings;
    }
}