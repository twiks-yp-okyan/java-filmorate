package ru.yandex.practicum.filmorate.storage.rating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class RatingStorageDb extends BaseRepository<Rating> implements RatingStorage {
    private static final String FIND_ALL_RATINGS = "SELECT * FROM rating";
    private static final String FIND_RATING_BY_ID = "SELECT * FROM rating WHERE id = ?";

    public RatingStorageDb(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Rating> getRatings() {
        return findMany(FIND_ALL_RATINGS);
    }

    public Optional<Rating> getRatingById(Integer id) {
        return findOne(FIND_RATING_BY_ID, id);
    }
}
