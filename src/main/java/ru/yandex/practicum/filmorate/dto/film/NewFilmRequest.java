package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.rating.RatingFilm;
import ru.yandex.practicum.filmorate.validator.annotation.DateAfterSpecial;

import java.time.LocalDate;

@Data
public class NewFilmRequest {
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @DateAfterSpecial(minDate = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private RatingFilm mpa;
}
