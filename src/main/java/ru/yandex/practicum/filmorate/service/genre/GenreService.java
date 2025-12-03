package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.genre.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;
    private final FilmGenreStorage filmGenreStorage;

    public Collection<GenreDto> getGenres() {
        return genreStorage.getGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }

    public GenreDto getGenreById(int id) {
        return genreStorage.getGenreById(id)
                .map(GenreMapper::mapToGenreDto)
                .orElseThrow(() -> new NotFoundException("Жанр не найден."));
    }

    public void saveFilmGenre(long filmId, int genreId) {
        filmGenreStorage.addFilmGenre(filmId, getGenreById(genreId).getId());
    }

    public List<GenreDto> findAllFilmGenres(long filmId) {
        return genreStorage.findAllFilmGenresByFilmId(filmId).stream()
                .map(GenreMapper::mapToGenreDto)
                .toList();
    }
}
