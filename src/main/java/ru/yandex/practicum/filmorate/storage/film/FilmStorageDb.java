package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Repository("filmStorageDb")
public class FilmStorageDb extends BaseRepository<Film> implements FilmStorage {
    private final static String FIND_ALL_FILMS_QUERY = "SELECT * FROM FILMS";
    private final static String FIND_FILM_BY_ID = "SELECT * FROM FILMS WHERE id = ?";
    private final static String INSERT_NEW_FILM_QUERY = "INSERT INTO films (name, description, release_date, duration) " +
            "VALUES (?, ?, ?, ?)";
    private final static String UPDATE_FILM_QUERY = "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ? " +
            "WHERE id = ?";
    private final static String FIND_FILM_BY_NAME_AND_RELEASE_DATE_QUERY = "SELECT * FROM films " +
            "WHERE name = ? AND release_date = ?";

    public FilmStorageDb(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Film> getFilms() {
        return findMany(FIND_ALL_FILMS_QUERY);
    }

    public Film getFilmById(Long id) {
        return findOne(FIND_FILM_BY_ID, id).orElseThrow(
                () -> new NotFoundException(String.format("Фильм с id = %d не найден", id))
        );
    }

    public Film addFilm(Film film) {
        long id = insert(
                INSERT_NEW_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration()
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
                film.getId()
        );
        return film;
    }

    public Optional<Film> findByNameAndReleaseDate(Film film) {
        return findOne(FIND_FILM_BY_NAME_AND_RELEASE_DATE_QUERY, film.getName(), film.getReleaseDate());
    }
}
