package ru.yandex.practicum.filmorate.storage.film.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

@Repository
public class FilmLikeStorageDb extends BaseRepository<FilmLike> implements FilmLikeStorage {
    private static final String INSERT_LIKE_QUERY = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";

    public FilmLikeStorageDb(JdbcTemplate jdbc, RowMapper<FilmLike> mapper) {
        super(jdbc, mapper);
    }

    public void addLike(long filmId, long userId) {
        insert_without_return(INSERT_LIKE_QUERY, filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        delete(DELETE_LIKE_QUERY, filmId, userId);
    }
}
