package ru.yandex.practicum.filmorate.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorInfo {
    // описание ошибки
    private final String error;
}