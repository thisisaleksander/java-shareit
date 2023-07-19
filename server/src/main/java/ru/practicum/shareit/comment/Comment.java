package ru.practicum.shareit.comment;

import lombok.Data;
import ru.practicum.shareit.item.Item;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Item item;
    @Column(name = "author_name")
    private String authorName;
    @Column(name = "created")
    LocalDateTime created;
    @NotBlank
    @Column(name = "text")
    private String text;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && Objects.equals(item, comment.item) && Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, text);
    }
}