package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.FriendshipException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.friendship.FriendshipService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipService friendshipService;

    public UserService(@Qualifier("userStorageDb") UserStorage userStorage, FriendshipService friendshipService) {
        this.userStorage = userStorage;
        this.friendshipService = friendshipService;
    }

    public Collection<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto getUserById(long id) {
        return userStorage.getUserById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", id)));
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

    public UserDto updateUser(UpdateUserRequest request) {
        userStorage.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(request.getId())) {
                        throw new ConditionsNotMetException("Пользователь с таким email уже существует.");
                    }
                });
        userStorage.findByLogin(request.getLogin())
                .ifPresent(user -> {
                    if (!user.getId().equals(request.getId())) {
                        throw new ConditionsNotMetException("Пользователь с таким логином уже существует.");
                    }
                });
        User userForUpdate = userStorage.getUserById(request.getId())
                .map(user -> UserMapper.updateUserData(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userForUpdate = userStorage.updateUser(userForUpdate);
        return UserMapper.mapToUserDto(userForUpdate);
    }

    public void startFriendship(Long userId, Long friendId) {
        // для проверки наличия таких пользователей
        userId = getUserById(userId).getId();
        friendId = getUserById(friendId).getId();
        friendshipService.addFriend(userId, friendId);
    }

    public void approveFriendship(long userId, long friendId) {
        // для проверки наличия таких пользователей
        userId = getUserById(userId).getId();
        friendId = getUserById(friendId).getId();
        friendshipService.approveFriend(userId, friendId);
    }

    public void endFriendship(Long userId, Long friendId) {
        // для проверки наличия таких пользователей
        userId = getUserById(userId).getId();
        friendId = getUserById(friendId).getId();
        friendshipService.deleteFromFriends(userId, friendId);
    }

    public List<UserDto> getMutualFriends(Long user1Id, Long user2Id) {
        if (user1Id.equals(user2Id)) {
            throw new FriendshipException(user1Id, user2Id,
                    "Одинаковые id пользователей для поиска общих друзей");
        }

        Collection<User> user1Friends = userStorage.getUserFriends(user1Id);
        Collection<User> user2Friends = userStorage.getUserFriends(user2Id);
        return user1Friends.stream()
                .filter(user2Friends::contains)
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public Collection<UserDto> getUserFriends(long userId) {
        UserDto user = getUserById(userId);
        return userStorage.getUserFriends(user.getId()).stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

//    private boolean isAlreadyFriends(Set<Long> user1Friends, Long user2Id) {
//        return user1Friends.contains(user2Id);
//    }
}
