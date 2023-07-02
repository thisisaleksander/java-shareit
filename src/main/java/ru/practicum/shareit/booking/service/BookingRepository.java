package ru.practicum.shareit.booking.service;

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

    @Query("select b from Booking as b " +
            "join b.booker as u where u.id = ?1 " +
            "order by b.start desc")

    List<Booking> findByUserId(long userId);

    @Query("select b from Booking as b " +
            "join b.booker as u where u.id = ?1 and b.status like ?2 " +
            "order by b.start desc")
    List<Booking> getBookingByUserIdAndByStatusContainingIgnoreCase(long userId, String state);

    @Query("select b from Booking as b " +
            "join b.booker as u where u.id = ?1 and b.start < current_timestamp and b.end > current_timestamp  " +
            "order by b.start desc")
    List<Booking> getCurrentByUserId(long userId);

    @Query("select b from Booking as b " +
            "join b.booker as u where u.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> getBookingByUserIdAndFinishAfterNow(long userId);

    @Query("select b from Booking as b " +
            "join b.booker as u where u.id = ?1 and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> getBookingByUserIdAndStarBeforeNow(long userId);


    @Query("select b  from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "order by b.start desc")
    List<Booking> findByOwnerId(long userId);

    @Query("select b  from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 and b.status like ?2 " +
            "order by b.start desc")
    List<Booking> getBookingByOwnerIdAndByStatusContainingIgnoreCase(long userId, String state);

    @Query("select b from Booking as b " +
            "join b.item as i " +
            "join i.user as u where u.id = ?1 and b.start < current_timestamp and b.end > current_timestamp " +
            "order by b.start desc")
    List<Booking> getCurrentByOwnerId(long userId);

    @Query("select b from Booking as b " +
            "join b.item as i " +
            "join i.user as u where u.id = ?1 and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> getPastByOwnerId(long userId);

    @Query("select b from Booking as b " +
            "join b.item as i " +
            "join i.user as u where u.id = ?1 and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> getBookingByOwnerIdAndStarBeforeNow(long userId);
}
