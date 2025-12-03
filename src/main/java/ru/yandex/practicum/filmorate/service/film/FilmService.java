package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.rating.RatingService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final RatingService ratingService;
    private final GenreService genreService;
//    private final UserStorage userStorage;
//    private final int topFilmCountConstantWithFuckingCheckstyleTermsNaming = 10;
//    private final Comparator<Film> filmLikesComparator = Comparator.comparing((Film film) -> film.getUserIdsLikes().size());

    public FilmService(
            @Qualifier("filmStorageDb") FilmStorage filmStorage,
            RatingService ratingService,
            GenreService genreService
    ) {
        this.filmStorage = filmStorage;
        this.ratingService = ratingService;
        this.genreService = genreService;
    }

    public Collection<FilmDto> getFilms() {
        return filmStorage.getFilms().stream()
                .map(this::getFilmWithGenres)
                .toList();
    }

    public FilmDto getFilmById(long id) {
        return filmStorage.getFilmById(id)
                .map(this::getFilmWithGenres)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id = %d не найден", id)));
    }

    public FilmDto saveFilm(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilmSave(request);

        if (film.getMpa() != null) {
            ratingService.getRatingById(film.getMpa().getId());
        }
        if (filmStorage.getFilms().contains(FilmMapper.mapToFilmSave(request))) {
            throw new ConditionsNotMetException("Фильм с таким названием и датой выхода уже существует.");
        }

        film = filmStorage.addFilm(film);
        final Long filmId = film.getId();
        request.getGenres().forEach(genre -> genreService.saveFilmGenre(filmId, genre.getId()));

        return getFilmWithGenres(film);
    }

    public FilmDto updateFilm(UpdateFilmRequest request) {
        Film filmForUpdate = filmStorage.getFilmById(request.getId())
                .map(film -> FilmMapper.updateFilmData(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм не найден."));

        if (filmStorage.getFilms().contains(FilmMapper.mapToFilmUpd(request))) {
            throw new ConditionsNotMetException("Фильм с таким названием и датой выхода уже существует.");
        }

        filmForUpdate = filmStorage.updateFilm(filmForUpdate);
        updateFilmGenres(filmForUpdate, request);

        return getFilmWithGenres(filmForUpdate);
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
    private FilmDto getFilmWithGenres(Film film) {
        return FilmMapper.mapToFilmDto(film, genreService.findAllFilmGenres(film.getId()));
    }

    private void updateFilmGenres(Film film, UpdateFilmRequest request) {
        request.getGenres().stream()
                .filter(genre -> !genreService.findAllFilmGenres(film.getId()).contains(genre))
                .forEach(genre -> genreService.saveFilmGenre(film.getId(), genre.getId()));
    }
}
