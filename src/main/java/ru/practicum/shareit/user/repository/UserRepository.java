package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    User create(User user);

    User get(long id);

    User get(String email);

    User update(User user);

    void remove(long id);

}
