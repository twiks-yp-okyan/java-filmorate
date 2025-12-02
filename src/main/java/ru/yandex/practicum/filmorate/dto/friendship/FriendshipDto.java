package ru.yandex.practicum.filmorate.dto.friendship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FriendshipDto {
    private Long userId;
    private Long friendId;
    private Boolean status;
}
