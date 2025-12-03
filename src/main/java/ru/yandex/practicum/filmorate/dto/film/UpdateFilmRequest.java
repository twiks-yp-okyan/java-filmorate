package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreRequest;
import ru.yandex.practicum.filmorate.dto.rating.RatingRequest;
import ru.yandex.practicum.filmorate.validator.annotation.DateAfterSpecial;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateFilmRequest {
    @NotNull
    private Long id;
    private String name;
    @Size(max = 200)
    private String description;
    @DateAfterSpecial(minDate = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private RatingRequest mpa;
    private List<GenreRequest> genres;

    public Boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public Boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public Boolean hasReleaseDate() {
        return releaseDate != null;
    }

    public Boolean hasDuration() {
        return duration != null;
    }

    public Boolean hasRatingMpaId() {
        return mpa != null;
    }
}
