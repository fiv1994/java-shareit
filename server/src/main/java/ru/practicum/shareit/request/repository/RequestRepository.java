package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByCreatorIdOrderByCreatedDesc(long creatorId);

    List<ItemRequest> findByCreatorIdNotOrderByCreatedDesc(long creatorId);

    List<ItemRequest> findByIdIn(List<Long> ids);
}