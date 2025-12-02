package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getUsers() {
        return users.values();
    }

    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    public User createUser(User user) {
        user.setId(IdGenerator.getNextId(users));
        // имя для отображения может быть пустым — в таком случае будет использован логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("User {} with id = {} was added", user.getLogin(), user.getId());
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
            if (!user.getName().isBlank()) {
                oldUser.setName(user.getName());
            }
            oldUser.setBirthday(user.getBirthday());
            log.info("User with id = {} was updated", user.getId());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
    }

    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    public Optional<User> findByLogin(String login) {
        return Optional.empty();
    }

    public List<User> getUserFriends(long userId) {
        return null;
    }
}
