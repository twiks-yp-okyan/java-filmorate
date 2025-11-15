package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return filmStorage.getFilmById(id);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Map<String, String> addLike(
            @PathVariable long id,
            @PathVariable long userId
    ) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Map<String, String> removeLike(
            @PathVariable long id,
            @PathVariable long userId
    ) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", required = false) Integer count) {
        return filmService.getTopFilms(count);
    }

}
