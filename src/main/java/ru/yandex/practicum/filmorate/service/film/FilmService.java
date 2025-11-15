package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.FilmLikeException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final int TOP_FILMS_COUNT = 10;
    private final Comparator<Film> filmLikesComparator = Comparator.comparing((Film film) -> film.getUserIdsLikes().size());

    public Map<String, String> addLike(Long filmId, Long userId) {
        Set<Long> filmLikes = new HashSet<>(filmStorage.getFilmById(filmId).getUserIdsLikes());
        if (filmLikes.contains(userId)) {
            throw new FilmLikeException(filmId, userId);
        }
        filmLikes.add(userId);
        filmStorage.getFilmById(filmId).setUserIdsLikes(filmLikes);
        return Map.of("result", String.format("Пользователь с id %d поставил лайк фильму с id %d", userId, filmId));
    }

    public Map<String, String> removeLike(Long filmId, Long userId) {
        Set<Long> filmLikes = new HashSet<>(filmStorage.getFilmById(filmId).getUserIdsLikes());
        if (!filmLikes.contains(userId)) {
            throw new FilmLikeException(filmId, userId);
        }
        filmLikes.remove(userId);
        filmStorage.getFilmById(filmId).setUserIdsLikes(filmLikes);
        return Map.of("result", String.format("Пользователь с id %d убрал лайк с фильма с id %d", userId, filmId));
    }

    public Map<String, List<Film>> getTopFilms(Integer count) {
        if (count == null) { count = TOP_FILMS_COUNT; }
        if (count <= 0) {
            throw new IncorrectParameterException("count", count.toString());
        }
        List<Film> sortedFilms = filmStorage.getFilms().stream()
                .sorted(filmLikesComparator.reversed())
                .limit(count)
                .toList();
        return Map.of("result", sortedFilms);
    }
}
