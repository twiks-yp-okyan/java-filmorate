package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Collection;

public interface FilmGenreStorage {
    Collection<FilmGenre> findAllFilmGenresByFilmId(long filmId);

    void addFilmGenre(long filmId, int genreId);
}
