package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Optional;

public interface FriendshipStorage {
    Friendship addFriend(Friendship friendship);

    Friendship approveFriend(Friendship friendship);

    Optional<Friendship> getFriendshipStatus(Friendship friendship);
}
