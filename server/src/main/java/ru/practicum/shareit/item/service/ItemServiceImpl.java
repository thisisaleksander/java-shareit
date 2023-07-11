package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.service.CommentRepository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.UserNotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.ItemWithBooking;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.mapToBookingDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;
    UserService userService;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;

    DateUtils dateUtils = new DateUtils();

    @Override
    public Collection<ItemWithBooking> getItems(long userId, int from, int size) {
        validateUserId(userId);
        if (from < 0) {
            throw new ValidationException("from is negative");
        }
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        return mapToItemWithBooking(itemRepository.findByUserId(userId, page).toList());
    }

    @Override
    public ItemWithBooking getItemById(long userId, long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Вещь с ID =%d не найден", itemId)));
        BookingDto lastBooking;
        BookingDto nextBooking;
        if (userId == item.getUser().getId()) {
            Booking lastBookingForDto = bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
                            item.getId(),
                            dateUtils.now(),
                            "APPROVED")
                    .orElse(null);
            if (lastBookingForDto == null) {
                lastBooking = null;
            } else {
                lastBooking = mapToBookingDto(lastBookingForDto);
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
                nextBooking = mapToBookingDto(nextBookingForDto);
            }
        } else {
            lastBooking = null;
            nextBooking = null;
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return mapToItemWithBookingEntity(item, lastBooking, nextBooking, comments);
    }

    @Override
    public Item getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с ID =%d не найден", itemId)));
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto itemDto, long itemId) {
        validateUserId(userId);
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
        return mapToItemDto(item);
    }

    @Override
    public Item addNewItem(long userId, ItemDto itemDto) {
        if (itemDto.getAvailable() == null || !itemDto.getAvailable()) {
            throw new ValidationException("Вещь должна быть доступна");
        }
        validateUserId(userId);

        User user = userService.getUserById(userId);
        Item itemForSave = mapToItem(user, itemDto);

        return itemRepository.save(itemForSave);
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        validateUserId(userId);
        validateOwner(userId, itemRepository.findById(itemId).get());
        itemRepository.deleteByUserIdAndId(userId, itemId);
    }

    @Override
    public Comment addNewComment(long userId, Comment comment, long itemId) {
        List<Booking> bookings = bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(
                userId,
                itemId,
                dateUtils.now());
        if (!bookings.isEmpty()) {
            comment.setItem(itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found")));
            comment.setAuthorName(userService.getUserById(userId).getName());
            comment.setCreated(LocalDateTime.now());
            return commentRepository.save(comment);
        } else throw new ValidationException("Пользователь не брал в аренду вещь");
    }

    @Override
    public List<ItemDto> getItemByQuery(String query, int from, int size) {
        if (from < 0) {
            throw new ValidationException("from is negative");
        }
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "id");
        int pageIndex = from / size;
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);

        if (query.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> userItems = itemRepository.getItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, page);
        for (int i = 0; i < userItems.size(); i++) {
            if (!userItems.get(i).getAvailable()) {
                userItems.remove(i);
            }
        }
        return mapToItemDto(userItems);
    }

    private void validateOwner(long userId, Item item) {
        if (userId != item.getUser().getId()) {
            log.warn("[ItemService] -> user id is incorrect");
            throw new NotFoundException("User is not the owner");
        }
    }

    public void validateUserId(long userId) {
        try {
            userService.getUserById(userId);
        } catch (UserNotFoundException exception) {
            throw new UserNotFoundException(userId);
        }
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
                lastBooking = mapToBookingDto(lastBookingForDto);
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
                nextBooking = mapToBookingDto(nextBookingForDto);
            }
            List<Comment> comments = commentRepository.findAllByItemId(item.getId());
            itemWithBookings.add(mapToItemWithBookingEntity(item, lastBooking, nextBooking, comments));
        }
        return itemWithBookings;
    }
}