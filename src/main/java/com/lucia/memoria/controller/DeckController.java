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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("deck")
public class DeckController {
    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<DeckDTO>> getUserDecks(@PathVariable UUID userId) {
        return ResponseEntity.ok().body(deckService.getUserFullDeckTree(userId));
    }

    @PostMapping
    public ResponseEntity<DeckDTO> save(@RequestBody DeckMinimalDTO deckMinimalDTO) {
        return ResponseEntity.ok().body(deckService.saveDeck(deckMinimalDTO));
    }
}
