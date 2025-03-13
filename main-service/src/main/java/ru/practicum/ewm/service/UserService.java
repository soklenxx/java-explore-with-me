package ru.practicum.ewm.service;

import ru.practicum.ewm.entity.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    List<User> getUsersByIds(String ids, int from, int size);

    User getUserById(long id);

    void deleteUserById(long userId);
}
