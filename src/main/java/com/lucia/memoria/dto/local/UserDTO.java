package com.lucia.memoria.dto.local;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record UserDTO(
        @NotBlank
        String username,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", message = "Invalid email address")
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$", message = "Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, and be between 8 to 20 characters long.")
        String password,

        LocalDateTime createdAt,
        LocalDateTime lastLogin
) {
}
