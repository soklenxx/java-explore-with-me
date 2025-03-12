package ru.practicum.ewm.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public User addUser(User user) {
        checkUserAlreadyExists(user);
        return repository.save(user);
    }

    @Override
    @Transactional
    public List<User> getUsersByIds(String ids, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new RuntimeException("Параметр from не может быть меньше 1");
        }
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (ids == null) {
            return repository.findAll(page).stream()
                    .collect(Collectors.toList());
        } else {
            List<Long> userIds = parseIds(ids);
            return repository.findAllByIdIn(userIds, page);
        }
    }

    @Override
    @Transactional
    public void deleteUserById(long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException("User with id = " + userId + " not found.");
        }
        repository.deleteById(userId);
    }

    @Override
    @Transactional
    public User getUserById(long userId) {
        return repository.findById(userId).orElseThrow(() -> new NotFoundException("User with id = " + userId + " not found."));
    }

    public static List<Long> parseIds(@NotNull String ids) {
        return ids != null
                ? Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList()
                : null;
    }

    private void checkUserAlreadyExists(User user) {
        Optional<User> optionalUser = repository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            throw new ConflictException("User this email already exists");
        }
    }
}
