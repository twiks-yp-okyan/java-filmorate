package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendshipException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

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
