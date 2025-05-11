package com.lucia.memoria.dto.local;

import java.util.List;

public record DeckDTO(
        UserDTO userDTO,
        String name,
        DeckDTO parentDeck,
        List<DeckDTO> childDecks,
        List<CardDTO> cards
) {
}
