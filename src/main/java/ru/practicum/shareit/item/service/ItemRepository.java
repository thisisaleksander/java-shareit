package ru.practicum.shareit.item.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByOwnerId(Integer userId);

    Optional<Item> findById(Integer id);

    void deleteByOwnerIdAndId(Integer userId, Integer itemId);

    List<Item> getItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String query, String query1);
}
