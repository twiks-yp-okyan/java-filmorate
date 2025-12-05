package ru.yandex.practicum.filmorate.dto.genre;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FilmGenreDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long filmId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer genreId;
}
