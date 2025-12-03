package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.rating.RatingDto;
import ru.yandex.practicum.filmorate.dto.rating.RatingRequest;
import ru.yandex.practicum.filmorate.model.Rating;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RatingMapper {
    public static RatingDto mapToDto(Rating rating) {
        RatingDto dto = new RatingDto();
        dto.setId(rating.getId());
        dto.setName(rating.getName());
        return dto;
    }

    public static Rating mapToRating(RatingRequest request) {
        Rating rating = new Rating();
        rating.setId(request.getId());
        return rating;
    }
}
