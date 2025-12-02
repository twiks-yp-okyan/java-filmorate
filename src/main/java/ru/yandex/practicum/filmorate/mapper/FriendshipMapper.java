package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.friendship.FriendshipDto;
import ru.yandex.practicum.filmorate.model.Friendship;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FriendshipMapper {
    public static Friendship mapToFriendship(long userId, long friendId) {
        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(false);
        return friendship;
    }

    public static FriendshipDto mapToFriendshipDto(Friendship friendship) {
        FriendshipDto dto = new FriendshipDto();
        dto.setUserId(friendship.getUserId());
        dto.setFriendId(friendship.getFriendId());
        dto.setStatus(friendship.getStatus());
        return dto;
    }
}
