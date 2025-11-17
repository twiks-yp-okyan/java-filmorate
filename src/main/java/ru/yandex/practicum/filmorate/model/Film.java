package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.annotation.DateAfterSpecial;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = {"name", "releaseDate"})
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @DateAfterSpecial(minDate = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Set<Long> userIdsLikes = new HashSet<>();
}
