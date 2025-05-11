package com.lucia.memoria.dto.local;

import jakarta.validation.constraints.NotBlank;

public record FrontDTO(
        @NotBlank
        String content,

        @NotBlank
        CardDTO cardDTO
) {
}
