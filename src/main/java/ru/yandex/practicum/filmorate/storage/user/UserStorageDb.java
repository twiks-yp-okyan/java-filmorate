package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository("userStorageDb")
public class UserStorageDb extends BaseRepository<User> implements UserStorage {
    private final static String FIND_ALL_USERS_QUERY = "SELECT * FROM USERS";
    private final static String FIND_USER_BY_ID = "SELECT * FROM USERS WHERE id = ?";
    private final static String FIND_USER_BY_EMAIL = "SELECT * FROM USERS WHERE email = ?";
    private final static String FIND_USER_BY_LOGIN = "SELECT * FROM USERS WHERE login = ?";
    private final static String INSERT_QUERY = "INSERT INTO users (email, login, name, birthdate) " +
            "VALUES (?, ?, ?, ?)";
    private final static String UPDATE_QUERY = "UPDATE users " +
            "SET email = ?, login = ?, name = ?, birthdate = ? WHERE id = ?";
    private final static String FIND_USER_FRIENDS_QUERY = "SELECT u.* " +
            "FROM friendship f JOIN users u ON f.friend_id = u.id " +
            "WHERE f.user_id = ? and f.status = true";


    public UserStorageDb(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Collection<User> getUsers() {
        return findMany(FIND_ALL_USERS_QUERY);
    }

    public Optional<User> getUserById(Long id) {
        return findOne(FIND_USER_BY_ID, id);
    }

    public Optional<User> findByEmail(String email) {
        return findOne(FIND_USER_BY_EMAIL, email);
    }

    public Optional<User> findByLogin(String login) {
        return findOne(FIND_USER_BY_LOGIN, login);
    }

    public User createUser(User user) {
        Long idFromDb = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday())
        );
        user.setId(idFromDb);
        return user;
    }

    public User updateUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public Collection<User> getUserFriends(long userId) {
        return findMany(FIND_USER_FRIENDS_QUERY, userId);
    }
}
