package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Optional;

public interface FriendshipStorage {
    Friendship addFriend(long userId, long friendId);

    Friendship approveFriend(long userId, long friendId);

    Optional<Friendship> getFriendshipStatus(long userId, long friendId);

    Boolean deleteFromFriends(long userId, long friendId);
}
