package com.lucia.memoria.dto.local;

import jakarta.validation.constraints.NotBlank;

public record BackDTO(
        @NotBlank
        String content,

        @NotBlank
        CardDTO cardDTO
) {
}
