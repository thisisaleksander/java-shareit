package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.service.CommentRepository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static ru.practicum.shareit.item.mapper.ItemMapper.mapToItem;
import static ru.practicum.shareit.item.mapper.ItemMapper.mapToItemDto;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getItemsShouldThrowExceptionTest() {
        assertThrows(ValidationException.class,
                () -> itemService.getItemsBy(1L, -1, 10));
    }

    @Test
    void getItemsUserNotValidTest() {

        when(userService.getByUserId(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class,
                () -> itemService.getItemsBy(1L, 1, 10));
    }

    @Test
    void getItemByIdTest() {
        Item item1 = new Item();
        item1.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        Item actualItem = itemRepository.findById(1L).get();

        assertEquals(item1, actualItem);
    }

    @Test
    void updateItemTest() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Name");
        item1.setDescription("Description");
        item1.setAvailable(true);

        Item itemToUPD = new Item();
        itemToUPD.setId(1L);
        itemToUPD.setName("NameUPD");
        itemToUPD.setDescription("DescriptionUPD");
        itemToUPD.setAvailable(true);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        ItemDto actualItem = itemService.update(1L, mapToItemDto(itemToUPD), 1L);

        assertEquals(mapToItemDto(itemToUPD), actualItem);

        verify(itemRepository).save(itemToUPD);
    }

    @Test
    void updateItemTestShouldThrowException() {
        ItemDto item1 = new ItemDto();

        doThrow(NotFoundException.class).when(userService).getByUserId(anyLong());


        assertThrows(NotFoundException.class,
                () -> itemService.update(1L, item1, 1L));

        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void addNewItemTest() throws ValidationException {
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);
        User user = new User();
        user.setId(1L);
        Item item = mapToItem(user, itemDto);
        when(userService.getByUserId(1L)).thenReturn(user);
        when(itemRepository.save(item)).thenReturn(item);

        Item actualItem = itemService.add(1L, itemDto);

        assertEquals(item, actualItem);
    }

    @Test
    void addNewItemTestShouldThrowException() {
        ItemDto itemDto = new ItemDto();
        User user = new User();
        user.setId(1L);
        Item item = mapToItem(user, itemDto);

        assertThrows(ValidationException.class,
                () -> itemService.add(1L, itemDto));
        verify(itemRepository, never()).save(any(Item.class));

    }

    @Test
    void deleteItemTest() {
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.setId(1L);
        item.setUser(owner);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.delete(1L, 1L);
        verify(itemRepository).deleteByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    void deleteItemTestShouldThrowException() {
        User owner = new User();
        owner.setId(1L);
        Item item = new Item();
        item.toString();
        item.setId(1L);
        item.setUser(owner);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class,
                () -> itemService.delete(2L, 1L));

        verify(itemRepository, never()).deleteByUserIdAndId(anyLong(), anyLong());
    }

    @Test
    void addNewCommentTest() throws ValidationException {
        Comment comment = new Comment();
        comment.toString();
        User user = new User();
        user.setName("USER");
        Item item = new Item();
        List<Booking> bookings = new ArrayList<>();
        Booking booking = new Booking();
        bookings.add(booking);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthorName("USER");
        when(bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(bookings);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userService.getByUserId(1L)).thenReturn(user);
        when(commentRepository.save(comment)).thenReturn(comment);

        Comment actualComment = itemService.addComment(1L, comment, 1L);

        assertEquals(comment, actualComment);
    }

    @Test
    void addNewCommentTestShouldThrowValidationException() {
        Comment comment = new Comment();
        User user = new User();
        user.setName("USER");
        Item item = new Item();
        List<Booking> bookings = new ArrayList<>();
        Booking booking = new Booking();
        bookings.add(booking);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthorName("USER");

        assertThrows(ValidationException.class,
                () -> itemService.addComment(1L, comment, 1L));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void addNewCommentTestShouldThrowNotFoundException() {
        Comment comment = new Comment();
        User user = new User();
        user.setName("USER");
        Item item = new Item();
        List<Booking> bookings = new ArrayList<>();
        Booking booking = new Booking();
        bookings.add(booking);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthorName("USER");
        when(bookingRepository.getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(bookings);

        assertThrows(NotFoundException.class,
                () -> itemService.addComment(1L, comment, 1L));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void getItemByQueryTest() throws ValidationException {
        Item item = new Item();
        item.setAvailable(true);
        item.setName("Покойник");
        String query = "покой";
        List<Item> items = new ArrayList<>();
        items.add(item);
        List<ItemDto> itemsDto = new ArrayList<>();
        itemsDto.add(mapToItemDto(item));
        when(itemRepository.getItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(anyString(), anyString(), any(Pageable.class))).thenReturn(items);

        List<ItemDto> actualItems = itemService.findByText(query, 1, 5);

        assertEquals(itemsDto, actualItems);
    }

    @Test
    void getItemByQueryTestShouldThrowException() {
        Item item = new Item();
        item.setAvailable(true);
        item.setName("Покойник");
        String query = "покой";
        List<Item> items = new ArrayList<>();
        items.add(item);
        List<ItemDto> itemsDto = new ArrayList<>();
        itemsDto.add(mapToItemDto(item));

        assertThrows(ValidationException.class,
                () -> itemService.findByText(query, -1, 5));
    }
}