package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.dto.local.CardMinimalDTO;
import com.lucia.memoria.dto.local.DeckMinimalDTO;
import com.lucia.memoria.dto.local.ResponseDeckWithCardsDTO;
import com.lucia.memoria.mapper.CardMapper;
import com.lucia.memoria.mapper.DeckWithCardsMapper;
import com.lucia.memoria.model.Card;
import com.lucia.memoria.model.Deck;
import com.lucia.memoria.repository.CardRepository;
import com.lucia.memoria.repository.DeckRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CardService {
    private final DeckRepository deckRepository;
    private final CardMapper cardMapper;
    private final DeckWithCardsMapper deckWithCardsMapper;
    private final CardRepository cardRepository;

    public CardService(DeckRepository deckRepository, CardMapper cardMapper, DeckWithCardsMapper deckWithCardsMapper, CardRepository cardRepository) {
        this.deckRepository = deckRepository;
        this.cardMapper = cardMapper;
        this.deckWithCardsMapper = deckWithCardsMapper;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public CardMinimalDTO saveCard(CardDTO cardDTO) {
        Deck parent = null;
        DeckMinimalDTO deckDTO = cardDTO.getDeck();

        if(deckDTO.getPath() != null && !deckDTO.getPath().trim().isBlank()) {
            parent = deckRepository.findByPath(deckDTO.getPath().trim())
                    .orElseThrow(() -> new IllegalArgumentException("Parent deck path not found"));
        }

        Deck deck = deckRepository.findByPath(parent.getPath()).orElse(null);
        if(deck == null) {
            throw new IllegalArgumentException("Trget deck does not exists");
        }

        Card card = cardMapper.toEntity(cardDTO);
        card.setDeck(deck);

        return cardMapper.toMinimalDTO(cardRepository.saveAndFlush(card));
    }

    @Transactional
    public ResponseDeckWithCardsDTO getDeckWithFlashCards(String deckPath) {
        Deck deck = deckRepository.findByPath(deckPath)
                .orElseThrow(() -> new IllegalArgumentException("Deck not found: "+ deckPath));

        List<Card> cards = cardRepository.findAllByDeck(deck);

        return deckWithCardsMapper.toDTO(deck, cards);
    }
}
