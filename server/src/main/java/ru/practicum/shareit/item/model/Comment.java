package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(schema = "public", name = "comments")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author_id")
    private long authorId;

    @Column(name = "item_id")
    private long itemId;

    @Column(name = "text")
    private String text;

    @Column(name = "created")
    private LocalDateTime created;
}

