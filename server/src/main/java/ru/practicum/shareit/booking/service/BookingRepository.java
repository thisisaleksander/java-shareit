package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findById(long id);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(long id, LocalDateTime start, String status);

    Optional<Booking> findFirstByItemIdAndEndAfterAndStatusOrderByStartAsc(long id, LocalDateTime end, String status);

    Page<Booking> getByBookerIdOrderByStartDesc(long userId, Pageable page);

    Page<Booking> getByBookerIdAndStatusContainingIgnoreCaseOrderByStartDesc(long userId, String state, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.start < current_timestamp and b.end > current_timestamp  " +
            "order by b.start desc")
    Page<Booking> getCurrentByUserId(long userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc")
    Page<Booking> getBookingByUserIdAndFinishAfterNow(long userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    Page<Booking> getBookingByUserIdAndStarBeforeNow(long userId, Pageable page);


    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "order by b.start desc")
    Page<Booking> findByOwnerId(long userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.status like ?2 " +
            "order by b.start desc")
    Page<Booking> getBookingByOwnerIdAndByStatusContainingIgnoreCase(long userId, String state, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.start < current_timestamp and b.end > current_timestamp " +
            "order by b.start desc")
    Page<Booking> getCurrentByOwnerId(long userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc")
    Page<Booking> getPastByOwnerId(long userId, Pageable page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    Page<Booking> getBookingByOwnerIdAndStarBeforeNowOrderByStartDesc(long userId, Pageable page);

    List<Booking> getBookingByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(long userId, long end, LocalDateTime itemId);
}
