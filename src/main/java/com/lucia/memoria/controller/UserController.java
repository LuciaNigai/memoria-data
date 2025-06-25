package com.lucia.memoria.controller;

import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.dto.local.TemplateDTO;
import com.lucia.memoria.dto.local.UserDTO;
import com.lucia.memoria.service.local.DeckService;
import com.lucia.memoria.service.local.TemplateService;
import com.lucia.memoria.service.local.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

  private final UserService userService;
  private final DeckService deckService;
  private final TemplateService templateService;

  public UserController(UserService userService, DeckService deckService,
      TemplateService templateService) {
    this.userService = userService;
    this.deckService = deckService;
    this.templateService = templateService;
  }

  @PostMapping
  public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
    UserDTO savedUser = userService.createUser(userDTO);
    return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable UUID userId) {
    UserDTO user = userService.getUserById(userId);
    return ResponseEntity.ok(user);
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<UserDTO> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/{userId}/templates")
  public ResponseEntity<List<TemplateDTO>> getUserTemplates(@PathVariable UUID userId) {
    return ResponseEntity.ok().body(templateService.getTemplatesByUserId(userId));
  }

  @GetMapping("/{userId}/decks")
  public ResponseEntity<List<DeckDTO>> getUserDecks(@PathVariable UUID userId) {
    return ResponseEntity.ok().body(deckService.getDecksByUserId(userId));
  }
}
