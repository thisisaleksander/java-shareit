package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto itemDto,
                          @RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody @Valid ItemDto itemDto,
                          @PathVariable(value = "itemId") Integer itemId,
                          @RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getBy(@PathVariable(value = "itemId") Integer itemId) {
        return itemService.getBy(itemId);
    }

    @GetMapping
    public Set<ItemDto> findBy(@RequestHeader(value = "X-Sharer-User-Id") Integer userId) {
        return itemService.findBy(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> findBy(@RequestParam(value = "text") String text) {
        return itemService.findBy(text);
    }
}