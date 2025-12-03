package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository("filmStorageDb")
public class FilmStorageDb extends BaseRepository<Film> implements FilmStorage {
    private final static String FIND_ALL_FILMS_QUERY = "SELECT * FROM FILMS";
    private final static String FIND_FILM_BY_ID = "SELECT * FROM FILMS WHERE id = ?";
    private final static String INSERT_NEW_FILM_QUERY = "INSERT INTO films (name, description, release_date, duration, rating_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE_FILM_QUERY = "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
            "WHERE id = ?";
    private static final String GET_TOP_FILMS_QUERY = "SELECT id, name, description, release_date, duration, rating_id " +
            "FROM (" +
            "SELECT f.*, COUNT(fl.film_id) as cnt FROM films f LEFT JOIN films_likes fl ON f.id = fl.film_id " +
            "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rating_id " +
            ") tmp ORDER BY cnt DESC LIMIT ?";

    public FilmStorageDb(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Film> getFilms() {
        return findMany(FIND_ALL_FILMS_QUERY);
    }

    public Optional<Film> getFilmById(Long id) {
        return findOne(FIND_FILM_BY_ID, id);
    }

    public Film addFilm(Film film) {
        long id = insert(
                INSERT_NEW_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    public Film updateFilm(Film film) {
        update(
                UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    public Collection<Film> getTopFilms(int count) {
        return findMany(GET_TOP_FILMS_QUERY, count);
    }
}
