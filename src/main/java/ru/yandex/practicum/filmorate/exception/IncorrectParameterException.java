package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class IncorrectParameterException extends RuntimeException {
  private final String parameterName;
  private final String parameterValue;
}
