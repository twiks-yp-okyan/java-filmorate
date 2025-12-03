package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public FilmDto addFilm(@Valid @RequestBody NewFilmRequest film) {
        return filmService.saveFilm(film);
    }

    @PutMapping
    public FilmDto updateFilm(@Valid @RequestBody UpdateFilmRequest film) {
        return filmService.updateFilm(film);
    }

//    @PutMapping("/{id}/like/{userId}")
//    public Map<String, String> addLike(
//            @PathVariable long id,
//            @PathVariable long userId
//    ) {
//        return filmService.addLike(id, userId);
//    }
//
//    @DeleteMapping("/{id}/like/{userId}")
//    public Map<String, String> removeLike(
//            @PathVariable long id,
//            @PathVariable long userId
//    ) {
//        return filmService.removeLike(id, userId);
//    }
//
//    @GetMapping("/popular")
//    public List<Film> getTopFilms(@RequestParam(value = "count", required = false) Integer count) {
//        return filmService.getTopFilms(count);
//    }

}
