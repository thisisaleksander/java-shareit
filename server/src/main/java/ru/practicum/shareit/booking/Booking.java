package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User booker;
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    @Column
    String status;
    @Column(name = "start")
    LocalDateTime start;
    @Column(name = "finish")
    LocalDateTime end;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id)
                && status == booking.status
                && Objects.equals(start, booking.start)
                && Objects.equals(end, booking.end)
                && Objects.equals(booker, booking.booker)
                && Objects.equals(item, booking.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, start, end, booker, item);
    }

}