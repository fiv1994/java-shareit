package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(long userId);

    @Query("""
            SELECT it
            FROM Item as it
            WHERE (it.name ILIKE :searchText OR it.description ILIKE :searchText)
            AND it.available = TRUE
            """)
    List<Item> findAvailableWhoseNameOrDescContainsText(String searchText);
}