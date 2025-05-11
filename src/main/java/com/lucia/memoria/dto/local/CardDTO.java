package com.lucia.memoria.dto.local;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CardDTO(

        @NotNull
        DeckDTO deckDTO,

        @NotNull
        CardTypeDTO cardTypeDTO,

        FrontDTO frontDTO,
        List<BackDTO> backList
) {
}
