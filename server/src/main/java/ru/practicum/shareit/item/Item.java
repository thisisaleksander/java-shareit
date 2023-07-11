package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "count")
    long count;
    @ManyToOne
    private User user;
    @Column(name = "available")
    Boolean available;
    @Column (name = "request_id")
    long  requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id)
                && Objects.equals(name, item.name)
                && Objects.equals(description, item.description)
                && Objects.equals(count, item.count)
                && Objects.equals(user, item.user)
                && Objects.equals(available, item.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, count, user, available);
    }
}
