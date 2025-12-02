package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.friendship.FriendshipDto;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDto;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody NewUserRequest user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable long id, @Valid @RequestBody UpdateUserRequest user) {
        return userService.updateUser(id, user);
    }

    @GetMapping("/{id}/friends")
    public Collection<UserDto> getUserFriends(@PathVariable Long id) {
        return userService.getUserFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public FriendshipDto startFriendship(
            @PathVariable long id,
            @PathVariable long friendId
    ) {
        return userService.startFriendship(id, friendId);
    }

    @PutMapping("/{id}/friends/{friendId}/approve")
    public FriendshipDto approveFriendship(
            @PathVariable long id,
            @PathVariable long friendId
    ) {
        return userService.approveFriendship(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Map<String, String> endFriendship(
            @PathVariable long id,
            @PathVariable long friendId
    ) {
        return userService.endFriendship(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDto> getMutualFriends(
            @PathVariable long id,
            @PathVariable long otherId
    ) {
        return userService.getMutualFriends(id, otherId);
    }

}
