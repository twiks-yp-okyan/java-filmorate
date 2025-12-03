package ru.yandex.practicum.filmorate.service.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendshipException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipStorage friendshipStorage;

    public void addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new FriendshipException(userId, friendId, "Нельзя отправить запрос на добавление в друзья самому себе");
        }
        if (friendshipStorage.getFriendshipStatus(userId, friendId).isPresent()) {
            throw new FriendshipException(userId, friendId
                    , "Запрос на дружбу уже отправлен от пользователя с id = %d пользователю с id = %d."
            );
        } else {
            friendshipStorage.addFriend(userId, friendId);
        }
    }

    public void approveFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new FriendshipException(userId, friendId, "Нельзя добавить в друзья самого себя");
        }
        Optional<Friendship> friendshipData = friendshipStorage.getFriendshipStatus(friendId, userId);
        if (friendshipData.isPresent()) {
            if (friendshipData.get().getStatus()) {
                throw new FriendshipException(friendshipData.get().getFriendId(), friendshipData.get().getUserId(),
                        "Пользователь с id = %d уже в списке друзей пользователя с id = %d");
            } else {
                friendshipStorage.approveFriend(friendId, userId);

            }
        } else {
            throw new FriendshipException(userId, friendId,
                    "Пользователь с id = %d не отправлял запроса на дружбу пользователю с id = %d");
        }
    }

    public void deleteFromFriends(long userId, long friendId) {
        friendshipStorage.deleteFromFriends(userId, friendId);
    }
}
