package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
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
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Long id) {
        return userService.getUserFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Map<String, String> startFriendship(
            @PathVariable long id,
            @PathVariable long friendId
    ) {
        return userService.startFriendship(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Map<String, String> endFriendship(
            @PathVariable long id,
            @PathVariable long friendId
    ) {
        return userService.endFriendship(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(
            @PathVariable long id,
            @PathVariable long otherId
    ) {
        return userService.getMutualFriends(id, otherId);
    }

}
