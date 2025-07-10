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
import java.util.Optional;
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
          .orElseThrow(() -> new NotFoundException("Parent path was not found"));
    }

    String dtoName = Optional.ofNullable(dto.getName())
        .orElseThrow(() -> new IllegalArgumentException("The deck name cannot be empty"));

    String newPath = parent == null ? dtoName : path + "::" + dtoName;

    if (deckRepository.findByPathAndUser(newPath, user).isPresent()) {
      throw new IllegalArgumentException("Deck already exists");
    }

    AccessLevel accessLevel = determineDeckAccessLevel(dto.getAccessLevel(), parent);

    Deck deck = new Deck(UUID.randomUUID(), user, dtoName, accessLevel, parent, newPath);
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
        .orElseThrow(() -> new NotFoundException("Deck not found."));
  }

  @Transactional(readOnly = true)
  public List<DeckDTO> getDecksByUserId(UUID userId) {
    User user = userService.getUserEntityById(userId);

    List<Deck> allDecks = deckRepository.findAllByUser(user);
    Map<String, DeckDTO> map = new HashMap<>();

    for (Deck deck : allDecks) {
      String path = deck.getPath();
      if (path == null) {
        log.warn("Deck with null path found: {}", deck.getDeckId());
        continue;
      }
      DeckDTO dto = deckMapper.toDTO(deck);
      dto.setChildDecks(new ArrayList<>());
      map.put(path, dto);
    }

    List<DeckDTO> roots = new ArrayList<>();

    for (Deck deck : allDecks) {
      String path = deck.getPath();
      DeckDTO currentDTO = map.get(path);
      Deck parentDeck = deck.getParentDeck();

      if (parentDeck == null || parentDeck.getPath() == null) {
        roots.add(currentDTO);
      } else {
        DeckDTO parentDTO = map.get(parentDeck.getPath());
        if (parentDTO != null) {
          parentDTO.getChildDecks().add(currentDTO);
        } else {
          log.warn("Parent deck missing for: {}", path);
          roots.add(currentDTO);
        }
      }
    }

    return roots;
  }


  @Transactional(readOnly = true)
  public DeckDTO getDeckById(UUID deckID) {
    return deckMapper.toDTO(getDeckEntityById(deckID));
  }

  @Transactional
  public void deleteDeck(UUID deckId, boolean force) {
    Deck deck = deckRepository.findByDeckIdWithCards(deckId)
        .orElseThrow(() -> new NotFoundException("Deck you are trying to delete does not exist"));
    if (!deck.getCards().isEmpty() && !force) {
      throw new ConfirmationException(
          "This deck contains cards, are you sure you want to delete it?");
    }

    deckRepository.delete(deck);
  }
}
