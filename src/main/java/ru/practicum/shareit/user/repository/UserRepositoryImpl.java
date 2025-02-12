package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> storage = new HashMap<>();
    private long currentMaxId = 0;

    public User create(User user) {
        storage.put(++currentMaxId, user.withId(currentMaxId));
        return storage.get(currentMaxId);
    }

    public User get(long id) {
        return storage.get(id);
    }

    public User get(String email) {
        User result = null;
        for (User user : storage.values()) {
            if (user.getEmail().equals(email)) {
                result = user;
                break;
            }
        }
        return result;
    }

    public User update(User user) {
        long userId = user.getId();
        storage.put(userId, user);
        return get(userId);
    }

    public void remove(long id) {
        storage.remove(id);
    }
}
