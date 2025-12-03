package ru.yandex.practicum.filmorate.storage.film.like;

public interface FilmLikeStorage {
    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
}
