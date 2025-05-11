package com.lucia.memoria.dto.local;

import jakarta.validation.constraints.NotNull;

public record CardTypeDTO (
        @NotNull
        String type
) {
}
