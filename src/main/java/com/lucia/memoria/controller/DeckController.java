package com.lucia.memoria.controller;

import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.dto.local.CardMinimalDTO;
import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.dto.local.DeckMinimalDTO;
import com.lucia.memoria.dto.local.ResponseDeckWithCardsDTO;
import com.lucia.memoria.service.local.CardService;
import com.lucia.memoria.service.local.DeckService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class DeckController {
    private final DeckService deckService;
    private final CardService cardService;

    public DeckController(DeckService deckService, CardService cardService) {
        this.deckService = deckService;
        this.cardService = cardService;
    }

    @GetMapping("/decks/{userId}")
    public ResponseEntity<List<DeckDTO>> getUserDecks(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(deckService.getUserFullDeckTree(userId));
    }

    @PostMapping("/decks")
    public ResponseEntity<DeckDTO> save(@RequestBody DeckMinimalDTO deckMinimalDTO) {
        return ResponseEntity.ok().body(deckService.saveDeck(deckMinimalDTO));
    }

    @PostMapping("/deck/card")
    public ResponseEntity<CardMinimalDTO> save(@RequestBody CardDTO cardDTO) {
        return ResponseEntity.ok().body(cardService.saveCard(cardDTO));
    }

    @GetMapping("/deck/cards")
    public ResponseEntity<ResponseDeckWithCardsDTO> getCards(@RequestParam String path) {
        return ResponseEntity.ok().body(cardService.getDeckWithFlashCards(path));
    }
}
