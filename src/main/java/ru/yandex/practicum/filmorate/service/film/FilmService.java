package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmLikeException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final int topFilmCountConstantWithFuckingCheckstyleTermsNaming = 10;
    private final Comparator<Film> filmLikesComparator = Comparator.comparing((Film film) -> film.getUserIdsLikes().size());

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Map<String, String> addLike(Long filmId, Long userId) {
        if (!userStorage.getUsers().contains(userStorage.getUserById(userId))) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Set<Long> filmLikes = new HashSet<>(filmStorage.getFilmById(filmId).getUserIdsLikes());
        if (filmLikes.contains(userId)) {
            throw new FilmLikeException(filmId, userId);
        }
        filmLikes.add(userId);
        filmStorage.getFilmById(filmId).setUserIdsLikes(filmLikes);
        return Map.of("result", String.format("Пользователь с id %d поставил лайк фильму с id %d", userId, filmId));
    }

    public Map<String, String> removeLike(Long filmId, Long userId) {
        if (!userStorage.getUsers().contains(userStorage.getUserById(userId))) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Set<Long> filmLikes = new HashSet<>(filmStorage.getFilmById(filmId).getUserIdsLikes());
        if (!filmLikes.contains(userId)) {
            throw new FilmLikeException(filmId, userId);
        }
        filmLikes.remove(userId);
        filmStorage.getFilmById(filmId).setUserIdsLikes(filmLikes);
        return Map.of("result", String.format("Пользователь с id %d убрал лайк с фильма с id %d", userId, filmId));
    }

    public List<Film> getTopFilms(Integer count) {
        if (count == null) {
            count = topFilmCountConstantWithFuckingCheckstyleTermsNaming;
        }
        if (count <= 0) {
            throw new IncorrectParameterException("count", count.toString());
        }
        return filmStorage.getFilms().stream()
                .sorted(filmLikesComparator.reversed())
                .limit(count)
                .toList();
    }
}
