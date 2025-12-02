package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Optional;

@Repository("friendshipStorageDb")
public class FriendshipStorageDb extends BaseRepository<Friendship> implements FriendshipStorage {
    private final static String INSERT_NEW_FRIEND_QUERY = "INSERT INTO friendship (user_id, friend_id, status) " +
            "VALUES (?, ?, false)";
    private final static String UPDATE_FRIENDSHIP_STATUS_QUERY = "UPDATE friendship " +
            "SET status = true WHERE user_id = ? and friend_id = ?";
    private final static String CHECK_FRIENDSHIP_STATUS_QUERY = "SELECT * FROM friendship WHERE " +
            "user_id = ? AND friend_id = ?";

    public FriendshipStorageDb(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);
    }

    public Friendship addFriend(Friendship friendship) {
        insert_without_return(INSERT_NEW_FRIEND_QUERY, friendship.getUserId(), friendship.getFriendId());
        return friendship;
    }

    public Friendship approveFriend(Friendship friendship) {
        update(UPDATE_FRIENDSHIP_STATUS_QUERY, friendship.getUserId(), friendship.getFriendId());
        friendship.setStatus(true);
        return friendship;
    }

    public Optional<Friendship> getFriendshipStatus(Friendship friendship) {
        return findOne(CHECK_FRIENDSHIP_STATUS_QUERY, friendship.getUserId(), friendship.getFriendId());
    }
}
