package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class FriendshipException extends RuntimeException {
  private final Long user1Id;
  private final Long user2Id;
  private final String message;

  public FriendshipException(Long user1Id, Long user2Id, String message) {
    this.user1Id = user1Id;
    this.user2Id = user2Id;
    this.message = String.format(message, user1Id, user2Id);
  }
}
