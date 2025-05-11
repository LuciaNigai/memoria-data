package com.lucia.memoria.controller;

import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.service.local.DeckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deck")
public class DeckController {

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("/all/{email}")
    public ResponseEntity<List<DeckDTO>> getUserDecks(@PathVariable String email) {
        return ResponseEntity.ok(deckService.getAllUserDecks(email));
    }
}
