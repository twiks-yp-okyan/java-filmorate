package ru.yandex.practicum.filmorate.service.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.rating.RatingDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingStorage ratingStorage;

    public Collection<RatingDto> getRatings() {
        return ratingStorage.getRatings().stream()
                .map(RatingMapper::mapToDto)
                .toList();
    }

    public RatingDto getRatingById(Integer id) {
        return ratingStorage.getRatingById(id)
                .map(RatingMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException(String.format("Рейтинга с id = %d не существует.", id)));
    }
}
