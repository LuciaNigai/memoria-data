package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.dto.local.DeckMinimalDTO;
import com.lucia.memoria.mapper.DeckMapper;
import com.lucia.memoria.model.Deck;
import com.lucia.memoria.model.User;
import com.lucia.memoria.repository.DeckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DeckService {

  private final DeckRepository deckRepository;
  private final UserService userService;
  private final DeckMapper deckMapper;

  public DeckService(DeckRepository deckRepository, UserService userService,
      DeckMapper deckMapper) {
    this.deckRepository = deckRepository;
    this.userService = userService;
    this.deckMapper = deckMapper;
  }

  @Transactional
  public DeckDTO createDeck(DeckMinimalDTO dto) {
    Deck parent = null;
    String path = dto.getPath();

    User user = userService.getUserEntityById(dto.getUserId());

    if (path != null && !path.isBlank()) {
      parent = deckRepository.findByPath(path)
          .orElseThrow(() -> new IllegalArgumentException("Parent path was not found"));
    }

    log.info("dto path {}", path);

    String newPath = parent == null ? dto.getName() : path + "::" + dto.getName();

    log.info("dto parent {}", parent);

    if (deckRepository.findByPathAndUser(newPath, user).isPresent()) {
      throw new IllegalArgumentException("Deck with that path already exists");
    }

    Deck deck = new Deck();
    deck.setDeckId(UUID.randomUUID());
    deck.setParentDeck(parent);
    deck.setPath(newPath);
    deck.setName(dto.getName());
    deck.setUser(user);

    Deck saved = deckRepository.save(deck);
    return deckMapper.toDTO(saved);
  }

  @Transactional(readOnly = true)
  public Deck getDeckEntityById(UUID deckId) {
    return deckRepository.findByDeckId(deckId)
        .orElseThrow(() -> new IllegalArgumentException("Wrong deck id"));
  }

  @Transactional(readOnly = true)
  public List<DeckDTO> getDecksByUserId(UUID userId) {
    User user = userService.getUserEntityById(userId);

    List<Deck> allDecks = deckRepository.findAllByUser(user);
    Map<String, DeckDTO> map = new HashMap<>();

    for (Deck deck : allDecks) {
      DeckDTO dto = deckMapper.toDTO(deck);
      dto.setChildDecks(new ArrayList<>());
      map.put(deck.getPath(), dto);
    }

    List<DeckDTO> roots = new ArrayList<>();

    for (Deck deck : allDecks) {
      DeckDTO current = map.get(deck.getPath());
      if (deck.getParentDeck() == null) {
        roots.add(current);
      } else {
        DeckDTO parentDTO = map.get(deck.getParentDeck().getPath());
        parentDTO.getChildDecks().add(current);
      }
    }

    return roots;
  }

}
