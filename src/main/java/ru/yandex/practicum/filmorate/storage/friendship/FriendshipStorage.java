package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Optional;

public interface FriendshipStorage {
    void addFriend(long userId, long friendId);

    void approveFriend(long userId, long friendId);

    Optional<Friendship> getFriendshipStatus(long userId, long friendId);

    void deleteFromFriends(long userId, long friendId);
}
