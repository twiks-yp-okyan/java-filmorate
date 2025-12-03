package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.film.like.FilmLikeStorage;

@Service
@RequiredArgsConstructor
public class FilmLikeService {
    private final FilmLikeStorage filmLikeStorage;

    public void addLike(long filmId, long userId) {
        filmLikeStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmLikeStorage.removeLike(filmId, userId);
    }
}
