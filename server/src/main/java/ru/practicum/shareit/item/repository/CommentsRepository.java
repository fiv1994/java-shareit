package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItemId(long itemId);

    @Query(value = """
            SELECT c.id,
                   c.author_id,
                   c.item_id,
                   c.text,
                   c.created
            FROM comments AS c
            JOIN items ON c.item_id = items.id
            WHERE items.owner_id = :userId;
            """, nativeQuery = true)
    List<Comment> findByItemsOwnerId(long userId);
}