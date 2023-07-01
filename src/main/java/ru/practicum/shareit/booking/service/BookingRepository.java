package ru.practicum.shareit.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Booking findBookingById(Integer id);

    @Query("select booking from Booking as booking join booking.booker as user " +
            "where user.id = ?1 order by booking.startTime desc")
    List<Booking> findByUserId(Integer userId);

    @Query("select booking from Booking as booking join booking.booker as user " +
            "where user.id = ?1 and booking.status like ?2 order by booking.startTime desc")
    List<Booking> getBookingByUserIdAndByStatusContainingIgnoreCase(Integer userId, String status);

    @Query("select booking from Booking as booking join booking.booker as user where user.id = ?1 " +
            "and booking.startTime <= current_timestamp and booking.endTime >= current_timestamp order by booking.startTime desc")
    List<Booking> getCurrentByUserId(Integer userId);

    @Query("select booking from Booking as booking join booking.booker as user where user.id = ?1 " +
            "and booking.endTime <= current_timestamp order by booking.startTime desc")
    List<Booking> getBookingByUserIdAndEndTimeAfterNow(Integer userId);

    @Query("select booking from Booking as booking join booking.booker as user where user.id = ?1 " +
            "and booking.startTime >= current_timestamp order by booking.startTime desc")
    List<Booking> getBookingByUserIdAndStarTimeBeforeNow(Integer userId);

    @Query("select booking from Booking as booking join booking.item as item join item.owner as owner " +
            "where owner.id = ?1 order by booking.startTime desc")
    List<Booking> findByOwner(Integer userId);

    @Query("select booking from Booking as booking join booking.item as item join item.owner as owner " +
            "where owner.id = ?1 and booking.status like ?2 order by booking.startTime")
    List<Booking> getBookingByOwnerIdAndByStatusContainingIgnoreCase(Integer userId, String state);

    @Query("select booking from Booking as booking join booking.item as item join item.owner as owner " +
            "where owner.id = ?1 and booking.startTime <= current_timestamp and booking.endTime >= current_timestamp " +
            "order by booking.startTime desc")
    List<Booking> getCurrentByOwner(Integer userId);

    @Query("select booking from Booking as booking join booking.item as item join item.owner as owner " +
            "where owner.id = ?1 and booking.endTime <= current_timestamp order by booking.startTime desc")
    List<Booking> getPastBookingByOwner(Integer userId);

    @Query("select booking from Booking as booking join booking.item as item join item.owner as owner " +
            "where owner.id = ?1 and booking.startTime >= current_timestamp order by booking.startTime desc")
    List<Booking> getBookingByOwnerAndStarTimeBeforeNow(Integer userId);

    Optional<Booking> findFirstByItemIdAndStartTimeBeforeAndStatusOrderByStartTimeDesc(Integer itemId,
                                                                                       LocalDateTime startTime,
                                                                                       String status);

    Optional<Booking> findFirstByItemIdAndEndTimeAfterAndStatusOrderByStartTimeAsc(Integer itemId,
                                                                                   LocalDateTime endTime,
                                                                                   String status);
}
