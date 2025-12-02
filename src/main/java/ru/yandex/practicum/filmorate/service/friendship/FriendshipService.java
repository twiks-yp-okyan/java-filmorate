package ru.yandex.practicum.filmorate.service.friendship;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.friendship.FriendshipDto;
import ru.yandex.practicum.filmorate.exception.FriendshipException;
import ru.yandex.practicum.filmorate.mapper.FriendshipMapper;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;

import java.util.Optional;

@Service
public class FriendshipService {
    @Qualifier("friendshipStorageDb")
    private final FriendshipStorage friendshipStorage;

    public FriendshipService(FriendshipStorage friendshipStorage) {
        this.friendshipStorage = friendshipStorage;
    }

    public FriendshipDto addFriend(long userId, long friendId) {
        Friendship friendship = FriendshipMapper.mapToFriendship(userId, friendId);

        if (friendship.getUserId().equals(friendship.getFriendId())) {
            throw new FriendshipException(
                    friendship.getUserId(),
                    friendship.getFriendId(),
                    "Нельзя отправить запрос на добавление в друзья самому себе");
        }
        if (friendshipStorage.getFriendshipStatus(friendship).isPresent()) {
            throw new FriendshipException(friendship.getUserId(), friendship.getFriendId()
                    , "Запрос на дружбу уже отправлен от пользователя с id = %d пользователю с id = %d."
            );
        } else {
            friendship = friendshipStorage.addFriend(friendship);
        }
        return FriendshipMapper.mapToFriendshipDto(friendship);
    }

    public FriendshipDto approveFriend(long userId, long friendId) {
        Friendship friendship = FriendshipMapper.mapToFriendship(friendId, userId);

        if (friendship.getUserId().equals(friendship.getFriendId())) {
            throw new FriendshipException(
                    friendship.getUserId(),
                    friendship.getFriendId(),
                    "Нельзя добавить в друзья самого себя");
        }
        Optional<Friendship> friendshipData = friendshipStorage.getFriendshipStatus(friendship);
        if (friendshipData.isPresent()) {
            if (friendshipData.get().getStatus()) {
                throw new FriendshipException(friendshipData.get().getFriendId(), friendshipData.get().getUserId(),
                        "Пользователь с id = %d уже в списке друзей пользователя с id = %d");
            } else {
                friendship = friendshipStorage.approveFriend(friendship);
            }
        } else {
            throw new FriendshipException(friendship.getUserId(), friendship.getFriendId(),
                    "Пользователь с id = %d не отправлял запроса на дружбу пользователю с id = %d");
        }
        return FriendshipMapper.mapToFriendshipDto(friendship);
    }
}
