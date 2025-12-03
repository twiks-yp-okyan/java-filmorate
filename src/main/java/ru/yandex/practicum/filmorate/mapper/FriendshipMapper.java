package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Friendship;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FriendshipMapper {
    public static Friendship mapToFriendshipRequest(long userId, long friendId) {
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(false);
        return friendship;
    }

    public static Friendship mapToFriendshipApprove(long userId, long friendId) {
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(true);
        return friendship;
    }
}
