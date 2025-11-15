package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.IdGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getFilms() {
        return films.values();
    }

    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new NotFoundException(String.format("Фильм с id %d не найден.", id));
    }

    public Film addFilm(Film film) {
        film.setId(IdGenerator.getNextId(films));
        films.put(film.getId(), film);
        log.info("Film {} with id = {} was added", film.getName(), film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setDuration(film.getDuration());
            log.info("Film with id = {} was updated", film.getId());
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }
}
