package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(schema = "public", name = "requests")
@Entity
@EqualsAndHashCode(of = {"id"})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "creatorId")
    private long creatorId;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private @With LocalDateTime created;

    @OneToMany
    @JoinTable(name = "requests_items",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    List<Item> items;

    public ItemRequest(long creatorId, String description) {
        this.id = 0;
        this.creatorId = creatorId;
        this.description = description;
        this.created = LocalDateTime.now();
        this.items = new ArrayList<>();
    }
}