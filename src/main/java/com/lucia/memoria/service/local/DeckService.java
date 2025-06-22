package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.dto.local.DeckMinimalDTO;
import com.lucia.memoria.mapper.DeckMapper;
import com.lucia.memoria.model.Deck;
import com.lucia.memoria.model.User;
import com.lucia.memoria.repository.DeckRepository;
import com.lucia.memoria.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class DeckService {

    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final DeckMapper deckMapper;

    public DeckService(DeckRepository deckRepository, UserRepository userRepository, DeckMapper deckMapper) {
        this.deckRepository = deckRepository;
        this.userRepository = userRepository;
        this.deckMapper = deckMapper;
    }

    @Transactional
    public DeckDTO saveDeck(DeckMinimalDTO dto) {
        Deck parent =null;
        String path = dto.getPath();

        Optional<User> user = userRepository.findByUserId(dto.getUserId());
        if(user.isEmpty()) {
            throw new IllegalArgumentException("User with such id is not found");
        }

        if(path != null && !path.isBlank()) {
            parent = deckRepository.findByPath(path)
                    .orElseThrow(() -> new IllegalArgumentException("Parent path was not found"));
        }

        log.info("dto path {}", path);

        String newPath = parent == null ? dto.getName() : path + "::" + dto.getName();

        log.info("dto parent {}", parent);

        if(deckRepository.findByPathAndUser(newPath, user.get()).isPresent()) {
            throw new IllegalArgumentException("Deck with that path already exists");
        }



        Deck deck = new Deck();
        deck.setDeckId(UUID.randomUUID());
        deck.setParentDeck(parent);
        deck.setPath(newPath);
        deck.setName(dto.getName());
        deck.setUser(user.get());

        Deck saved = deckRepository.save(deck);
        return deckMapper.toDTO(saved);
    }

    @Transactional
    public List<DeckDTO> getUserFullDeckTree(UUID userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if(user.isEmpty()) {
            throw new IllegalArgumentException("User is not valid");
        }

        List<Deck> allDecks = deckRepository.findAllByUser(user.get());
        Map<String, DeckDTO> map = new HashMap<>();

        for(Deck deck : allDecks) {
            DeckDTO dto = deckMapper.toDTO(deck);
            dto.setChildDecks(new ArrayList<>());
            map.put(deck.getPath(), dto);
        }

        List<DeckDTO> roots = new ArrayList<>();

        for(Deck deck : allDecks) {
            DeckDTO current = map.get(deck.getPath());
            if(deck.getParentDeck() == null) {
                roots.add(current);
            } else {
                DeckDTO parentDTO = map.get(deck.getParentDeck().getPath());
                parentDTO.getChildDecks().add(current);
            }
        }

        return roots;
    }

}
