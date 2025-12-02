package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.rating.RatingDto;
import ru.yandex.practicum.filmorate.dto.rating.RatingFilm;
import ru.yandex.practicum.filmorate.model.Rating;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RatingMapper {
    public static RatingDto mapToDto(Rating rating) {
        RatingDto dto = new RatingDto();
        dto.setId(rating.getId());
        dto.setName(rating.getName());
        return dto;
    }

    public static RatingFilm mapToRatingFilm(Integer id) {
        RatingFilm ratingFilm = new RatingFilm();
        ratingFilm.setId(id);
        return ratingFilm;
    }
}
