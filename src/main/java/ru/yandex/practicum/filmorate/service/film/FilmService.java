package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.FilmLikeException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
//    private final UserStorage userStorage;
//    private final int topFilmCountConstantWithFuckingCheckstyleTermsNaming = 10;
//    private final Comparator<Film> filmLikesComparator = Comparator.comparing((Film film) -> film.getUserIdsLikes().size());

    public FilmService(@Qualifier("filmStorageDb") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<FilmDto> getFilms() {
        return filmStorage.getFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    public FilmDto getFilmById(long id) {
        return FilmMapper.mapToFilmDto(filmStorage.getFilmById(id));
    }

    public FilmDto addFilm(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        Optional<Film> existedFilm = filmStorage.findByNameAndReleaseDate(film);
        if (existedFilm.isPresent()) {
            throw new ConditionsNotMetException("Фильм с таким названием и датой выхода уже существует.");
        }
        film = filmStorage.addFilm(film);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto updateFilm(long id, UpdateFilmRequest request) {
        Film filmForUpdate = filmStorage.getFilmById(id);
        Film filmUpdate = FilmMapper.updateFilmData(filmForUpdate, request);
        if (filmStorage.findByNameAndReleaseDate(filmUpdate).isPresent()) {
            throw new ConditionsNotMetException("Фильм с таким названием и датой выхода уже существует.");
        }
        filmForUpdate = filmStorage.updateFilm(filmUpdate);
        return FilmMapper.mapToFilmDto(filmForUpdate);
    }

//    public Map<String, String> addLike(Long filmId, Long userId) {
//        if (!userStorage.getUsers().contains(userStorage.getUserById(userId))) {
//            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
//        }
//        Set<Long> filmLikes = new HashSet<>(filmStorage.getFilmById(filmId).getUserIdsLikes());
//        if (filmLikes.contains(userId)) {
//            throw new FilmLikeException(filmId, userId);
//        }
//        filmLikes.add(userId);
//        filmStorage.getFilmById(filmId).setUserIdsLikes(filmLikes);
//        return Map.of("result", String.format("Пользователь с id %d поставил лайк фильму с id %d", userId, filmId));
//    }
//
//    public Map<String, String> removeLike(Long filmId, Long userId) {
//        if (!userStorage.getUsers().contains(userStorage.getUserById(userId))) {
//            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
//        }
//        Set<Long> filmLikes = new HashSet<>(filmStorage.getFilmById(filmId).getUserIdsLikes());
//        if (!filmLikes.contains(userId)) {
//            throw new FilmLikeException(filmId, userId);
//        }
//        filmLikes.remove(userId);
//        filmStorage.getFilmById(filmId).setUserIdsLikes(filmLikes);
//        return Map.of("result", String.format("Пользователь с id %d убрал лайк с фильма с id %d", userId, filmId));
//    }
//
//    public List<Film> getTopFilms(Integer count) {
//        if (count == null) {
//            count = topFilmCountConstantWithFuckingCheckstyleTermsNaming;
//        }
//        if (count <= 0) {
//            throw new IncorrectParameterException("count", count.toString());
//        }
//        return filmStorage.getFilms().stream()
//                .sorted(filmLikesComparator.reversed())
//                .limit(count)
//                .toList();
//    }
}
