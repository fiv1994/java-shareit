package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> storage = new HashMap<>();
    private ItemMapper itemMapper;
    private long currentMaxId = 0;

    @Override
    public Item create(Item item) {
        storage.put(++currentMaxId, item.withId(currentMaxId));
        return storage.get(currentMaxId);
    }

    @Override
    public Item update(Item item) {
        long itemId = item.getId();
        storage.put(itemId, item);
        return get(itemId);
    }

    @Override
    public Item get(long id) {
        return storage.get(id);
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Item> getAll(long userId) {
        return filterItems(item -> item.getOwnerId() == userId, getAll());
    }

    @Override
    public List<Item> getAllAvailable() {
        return filterItems(item -> item.getAvailable(), getAll());
    }

    @Override
    public List<Item> searchItems(String text) {
        String textLowerCase = text.toLowerCase();
        List<Item> allAvailableItems = getAllAvailable();
        return allAvailableItems.stream()
                .filter(item -> {
                    String nameLower = item.getName().toLowerCase();
                    String descLower = item.getDescription().toLowerCase();
                    return nameLower.contains(textLowerCase) || descLower.contains(textLowerCase);
                })
                .toList();
    }

    @Override
    public void remove(long id) {
        storage.remove(id);
    }

    private List<Item> filterItems(Predicate<Item> predicate, List<Item> items) {
        List<Item> copyToProcess = new ArrayList<>(items);
        copyToProcess.removeIf(predicate.negate());
        return copyToProcess;
    }
}