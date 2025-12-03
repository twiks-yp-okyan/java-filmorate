package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Optional;

@Repository("friendshipStorageDb")
public class FriendshipStorageDb extends BaseRepository<Friendship> implements FriendshipStorage {
    private static final String INSERT_NEW_FRIEND_QUERY = "INSERT INTO friendship (user_id, friend_id, status) " +
            "VALUES (?, ?, false)";
    private static final String UPDATE_FRIENDSHIP_STATUS_QUERY = "UPDATE friendship " +
            "SET status = true WHERE user_id = ? and friend_id = ?";
    private static final String CHECK_FRIENDSHIP_STATUS_QUERY = "SELECT * FROM friendship WHERE " +
            "user_id = ? AND friend_id = ?";
    private static final String DELETE_FROM_FRIENDS_QUERY = "DELETE FROM friendship WHERE user_id = ? and friend_id = ?";

    public FriendshipStorageDb(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    public void addFriend(long userId, long friendId) {
        insert_without_return(INSERT_NEW_FRIEND_QUERY, userId, friendId);
    }

    public void approveFriend(long userId, long friendId) {
        update(UPDATE_FRIENDSHIP_STATUS_QUERY, userId, friendId);
    }

    public Optional<Friendship> getFriendshipStatus(long userId, long friendId) {
        return findOne(CHECK_FRIENDSHIP_STATUS_QUERY, userId, friendId);
    }

    public void deleteFromFriends(long userId, long friendId) {
        delete(DELETE_FROM_FRIENDS_QUERY, userId, friendId);
    }
}
