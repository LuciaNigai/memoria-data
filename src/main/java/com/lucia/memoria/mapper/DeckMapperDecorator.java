package com.lucia.memoria.mapper;

import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.model.Deck;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeckMapperDecorator implements DeckMapper {

    private final DeckMapper delegate; // This is the original DeckMapper

    public DeckMapperDecorator(DeckMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public DeckDTO toDTO(Deck deck, DeckCycleAvoidingContext context) {
        if (deck == null) {
            return null;
        }

        // Check if the deck is already mapped
        DeckDTO existing = context.getMappedInstance(deck);
        if (existing != null) {
            return existing;
        }

        // Map the deck and store it in the context
        DeckDTO dto = delegate.toDTO(deck, context);
        context.storeMappedInstance(deck, dto);
        return dto;
    }

    @Override
    public List<DeckDTO> toDTO(List<Deck> decks, DeckCycleAvoidingContext context) {
        if (decks == null) {
            return null;
        }
        return delegate.toDTO(decks, context);
    }

    @Override
    public List<Deck> toEntity(List<DeckDTO> deckDTOList, DeckCycleAvoidingContext context) {
        return delegate.toEntity(deckDTOList, context);
    }

    @Override
    public DeckDTO mapParentDeck(Deck deck, DeckCycleAvoidingContext context) {
        return delegate.mapParentDeck(deck, context);
    }
}
