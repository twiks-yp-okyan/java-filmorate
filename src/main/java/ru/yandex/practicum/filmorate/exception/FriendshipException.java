package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FriendshipException extends RuntimeException {
  private final Long user1Id;
  private final Long user2Id;
}
