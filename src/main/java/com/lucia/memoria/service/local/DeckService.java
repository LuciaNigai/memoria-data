package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.dto.local.DeckMinimalDTO;
import com.lucia.memoria.exception.ConfirmationException;
import com.lucia.memoria.exception.NotFoundException;
import com.lucia.memoria.helper.AccessLevel;
import com.lucia.memoria.mapper.DeckMapper;
import com.lucia.memoria.model.Deck;
import com.lucia.memoria.model.User;
import com.lucia.memoria.repository.DeckRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    if (!StringUtils.isBlank(path)) {
      parent = deckRepository.findByPath(path)
          .orElseThrow(() -> new IllegalArgumentException("Parent path was not found"));
    }

    log.debug("dto path {}", path);

    String newPath = parent == null ? dto.getName() : path + "::" + dto.getName();

    log.debug("dto parent {}", parent);

    if (deckRepository.findByPathAndUser(newPath, user).isPresent()) {
      throw new IllegalArgumentException("Deck with that path already exists");
    }

    AccessLevel accessLevel = determineDeckAccessLevel(dto.getAccessLevel(), parent);

    Deck deck = new Deck();
    deck.setDeckId(UUID.randomUUID());
    deck.setParentDeck(parent);
    deck.setAccessLevel(accessLevel);
    deck.setPath(newPath);
    deck.setName(dto.getName());
    deck.setUser(user);

    Deck saved = deckRepository.save(deck);
    return deckMapper.toDTO(saved);
  }

  private AccessLevel determineDeckAccessLevel(AccessLevel dtoAccessLevel, Deck parent) {
    if (dtoAccessLevel == AccessLevel.DEFAULT) {
      if (parent == null) {
        return AccessLevel.PRIVATE;
      } else {
        return parent.getAccessLevel();
      }
    } else {
      return dtoAccessLevel == null ? AccessLevel.PRIVATE : dtoAccessLevel;
    }
  }

  @Transactional(readOnly = true)
  public Deck getDeckEntityById(UUID deckId) {
    return deckRepository.findByDeckId(deckId)
        .orElseThrow(() -> new IllegalArgumentException("Deck not found."));
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
        if (parentDTO != null) {
          parentDTO.getChildDecks().add(current);
        } else {
          log.warn("Parent deck missing for: {}", deck.getPath());
        }
      }
    }
    return roots;
  }

  @Transactional(readOnly = true)
  public DeckDTO getDeckById(UUID deckID) {
    return deckMapper.toDTO(getDeckEntityById(deckID));
  }

  public void deleteDeck(UUID deckId, boolean force) {
    Deck deck = deckRepository.findByDeckIdWithCards(deckId)
        .orElseThrow(() -> new NotFoundException("Deck you are trying to delete doe not exist"));
    if(!deck.getCards().isEmpty() && !force) {
        throw new ConfirmationException("This deck contains cards, are you sure you want to delete it?");
    }

    deckRepository.delete(deck);
  }
}
