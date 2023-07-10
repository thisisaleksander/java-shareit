package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByUserId(long userId, Pageable page);

    Optional<Item> findById(long id);

    void deleteByUserIdAndId(long userId, long itemId);

    List<Item> getItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String query, String query1, Pageable page);

    Optional<List<Item>> findByRequestId(long requestId);
}
