package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.FriendshipException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    @Qualifier("dbImplementation")
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public UserDto create(NewUserRequest request) {
        if (userStorage.findByEmail(request.getEmail()).isPresent()) {
            throw new ConditionsNotMetException("Пользователь с таким email уже существует.");
        }
        if (userStorage.findByLogin(request.getLogin()).isPresent()) {
            throw new ConditionsNotMetException("Пользователь с таким логином уже существует.");
        }
        User user = UserMapper.mapToUser(request);
        user = userStorage.createUser(user);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto updateUser(long id, UpdateUserRequest request) {
        userStorage.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new ConditionsNotMetException("Пользователь с таким email уже существует.");
                    }
                });
        userStorage.findByLogin(request.getLogin())
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new ConditionsNotMetException("Пользователь с таким логином уже существует.");
                    }
                });
        User userForUpdate = userStorage.getUserById(id);
        userForUpdate = userStorage.updateUser(UserMapper.updateUserData(userForUpdate, request));
        return UserMapper.mapToUserDto(userForUpdate);
    }

    public Map<String, String> startFriendship(Long user1Id, Long user2Id) {
        Set<Long> user1Friends = new HashSet<>(userStorage.getUserById(user1Id).getFriends());
        Set<Long> user2Friends = new HashSet<>(userStorage.getUserById(user2Id).getFriends());
        if (isAlreadyFriends(user1Friends, user2Id) || isAlreadyFriends(user2Friends, user1Id) || user1Id.equals(user2Id)) {
            throw new FriendshipException(user1Id, user2Id);
        }
        user1Friends.add(user2Id);
        userStorage.getUserById(user1Id).setFriends(user1Friends);
        user2Friends.add(user1Id);
        userStorage.getUserById(user2Id).setFriends(user2Friends);
        return Map.of("result", String.format("Пользователи с id %d и %d добавили друг друга в друзья.", user1Id, user2Id));
    }

    public Map<String, String> endFriendship(Long user1Id, Long user2Id) {
        Set<Long> user1Friends = new HashSet<>(userStorage.getUserById(user1Id).getFriends());
        Set<Long> user2Friends = new HashSet<>(userStorage.getUserById(user2Id).getFriends());
        if (!(isAlreadyFriends(user1Friends, user2Id) || isAlreadyFriends(user2Friends, user1Id)) || user1Id.equals(user2Id)) {
            throw new FriendshipException(user1Id, user2Id);
        }
        user1Friends.remove(user2Id);
        userStorage.getUserById(user1Id).setFriends(user1Friends);
        user2Friends.remove(user1Id);
        userStorage.getUserById(user2Id).setFriends(user2Friends);
        return Map.of("result", String.format("Пользователи с id %d и %d удалили друг друга из друзей.", user1Id, user2Id));
    }

    public List<User> getMutualFriends(Long user1Id, Long user2Id) {
        final Set<Long> user1Friends = new HashSet<>(userStorage.getUserById(user1Id).getFriends());
        final Set<Long> user2Friends = new HashSet<>(userStorage.getUserById(user2Id).getFriends());

        return userStorage.getUsers().stream()
                .filter(user -> user1Friends.contains(user.getId()) && user2Friends.contains(user.getId()))
                .toList();
    }

    public List<User> getUserFriends(long userId) {
        final User currentUser = userStorage.getUserById(userId);
        return userStorage.getUsers().stream()
                .filter(user -> currentUser.getFriends().contains(user.getId()))
                .toList();
    }

    private boolean isAlreadyFriends(Set<Long> user1Friends, Long user2Id) {
        return user1Friends.contains(user2Id);
    }
}
