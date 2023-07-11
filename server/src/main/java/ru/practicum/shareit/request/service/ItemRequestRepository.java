package ru.practicum.shareit.request.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Optional<ItemRequest> findById(long id);

    List<ItemRequest> findByUserId(long userId);
}
