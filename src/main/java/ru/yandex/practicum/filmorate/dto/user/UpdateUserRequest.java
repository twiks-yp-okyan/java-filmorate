package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    @Email
    private String email;
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public Boolean hasEmail() {
        return !(email == null || email.isBlank());
    }

    public Boolean hasLogin() {
        return !(login == null || login.isBlank());
    }

    public Boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public Boolean hasBirthday() {
        return birthday != null;
    }
}
