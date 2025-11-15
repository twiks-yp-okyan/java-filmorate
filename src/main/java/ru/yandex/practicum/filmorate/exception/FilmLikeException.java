package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FilmLikeException extends RuntimeException {
  private final long filmId;
  private final long userId;
}
