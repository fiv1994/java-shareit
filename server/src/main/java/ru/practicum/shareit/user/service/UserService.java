package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

public interface UserService {

    User createUser(User user);

    User getUser(long id);

    User patchUser(User user);

    void deleteUser(long id);
}
