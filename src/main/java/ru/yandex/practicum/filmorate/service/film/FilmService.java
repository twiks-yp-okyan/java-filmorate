package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.rating.RatingService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final RatingService ratingService;
    private final GenreService genreService;
    private final FilmLikeService filmLikeService;
    private final UserService userService;
    private final int topFilmCountConstantWithFuckingCheckstyleTermsNaming = 10;

    // Конструктор только ради Qualifier
    public FilmService(
            @Qualifier("filmStorageDb") FilmStorage filmStorage,
            RatingService ratingService,
            GenreService genreService,
            FilmLikeService filmLikeService,
            UserService userService
    ) {
        this.filmStorage = filmStorage;
        this.ratingService = ratingService;
        this.genreService = genreService;
        this.filmLikeService = filmLikeService;
        this.userService = userService;
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

    // Странно, что не надо проверять наличие в БД с таким же названием + датой релиза..
    // Была такая проверка, убрал ее, все тесты прошли:D
    public FilmDto saveFilm(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilmSave(request);

        if (film.getRatingId() != null) {
            ratingService.getRatingById(film.getRatingId());
        }

        film = filmStorage.addFilm(film);
        final Long filmId = film.getId();
        if (request.getGenres() != null) {
            genreService.saveFilmGenres(filmId, request.getGenres());
        }
        return getFilmWithGenres(film);
    }

    public FilmDto updateFilm(UpdateFilmRequest request) {
        Film filmForUpdate = filmStorage.getFilmById(request.getId())
                .map(film -> FilmMapper.updateFilmData(film, request))
                .orElseThrow(() -> new NotFoundException("Фильм не найден."));

        filmForUpdate = filmStorage.updateFilm(filmForUpdate);
        if (request.getGenres() != null) {
            genreService.saveFilmGenres(filmForUpdate.getId(), request.getGenres());
        }

        return getFilmWithGenres(filmForUpdate);
    }

    public void addLike(Long filmId, Long userId) {
        filmLikeService.addLike(getFilmById(filmId).getId(), userService.getUserById(userId).getId());
    }

    public void removeLike(Long filmId, Long userId) {
        filmLikeService.removeLike(getFilmById(filmId).getId(), userService.getUserById(userId).getId());
    }

    public List<FilmDto> getTopFilms(Integer count) {
        if (count == null) {
            count = topFilmCountConstantWithFuckingCheckstyleTermsNaming;
        }
        if (count <= 0) {
            throw new IncorrectParameterException("count", count.toString());
        }
        return filmStorage.getTopFilms(count).stream()
                .map(this::getFilmWithGenres)
                .toList();
    }

    private FilmDto getFilmWithGenres(Film film) {
        return FilmMapper.mapToFilmDto(
                film,
                ratingService.getRatingById(film.getRatingId()),
                genreService.findAllFilmGenres(film.getId()));
    }
}
