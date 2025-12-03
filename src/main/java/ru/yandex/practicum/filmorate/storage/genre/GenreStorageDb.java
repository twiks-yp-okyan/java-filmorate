package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreStorageDb extends BaseRepository<Genre> implements GenreStorage {
    private final static String FIND_ALL_GENRES_QUERY = "SELECT * FROM genres";
    private static final String FIND_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";
    private static final String INSERT_NEW_GENRE_QUERY = "INSERT INTO genres (name) VALUES (?)";
    private static final String FIND_GENRE_BY_NAME = "SELECT * FROM genres WHERE name = ?";

    public GenreStorageDb(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getGenres() {
        return findMany(FIND_ALL_GENRES_QUERY);
    }

    public Optional<Genre> getGenreById(long id) {
        return findOne(FIND_GENRE_BY_ID, id);
    }

    public Optional<Genre> getGenreByName(String name) {
        return findOne(FIND_GENRE_BY_NAME, name);
    }

    public void addGenre(String name) {
        insert_without_return(INSERT_NEW_GENRE_QUERY, name);
    }
}
