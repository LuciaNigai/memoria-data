package com.lucia.memoria.controller;

import com.lucia.memoria.dto.local.CardDTO;
import com.lucia.memoria.dto.local.CardMinimalDTO;
import com.lucia.memoria.service.local.CardService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("card")
public class CardController {

  private final CardService cardService;

  public CardController(CardService cardService) {
    this.cardService = cardService;
  }

  @PostMapping("")
  public ResponseEntity<CardMinimalDTO> save(@RequestBody CardMinimalDTO cardDTO) {
    return ResponseEntity.ok().body(cardService.saveCard(cardDTO));
  }

  @GetMapping("/{cardId}")
  public ResponseEntity<CardDTO> getCardById(@PathVariable UUID cardId) {
    return ResponseEntity.ok().body(cardService.getCardById(cardId));
  }
}
