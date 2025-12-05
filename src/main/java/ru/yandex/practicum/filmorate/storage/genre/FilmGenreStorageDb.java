package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.Collection;

@Repository
public class FilmGenreStorageDb extends BaseRepository<FilmGenre> implements FilmGenreStorage {
    private static final String FIND_ALL_FILM_GENRES_BY_FILM_ID = "SELECT * FROM films_genres WHERE film_id = ?";
    private static final String INSERT_NEW_FILM_GENRE_QUERY = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM films_genres WHERE film_id = ?";

    public FilmGenreStorageDb(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<FilmGenre> findAllFilmGenresByFilmId(long filmId) {
        return findMany(FIND_ALL_FILM_GENRES_BY_FILM_ID, filmId);
    }

    public void addFilmGenre(long filmId, int genreId) {
        insert_without_return(INSERT_NEW_FILM_GENRE_QUERY, filmId, genreId);
    }

    public void deleteFilmGenres(long filmId) {
        delete(DELETE_FILM_GENRES_QUERY, filmId);
    }
}
