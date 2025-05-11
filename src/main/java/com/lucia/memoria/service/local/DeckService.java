package com.lucia.memoria.service.local;

import com.lucia.memoria.dto.local.DeckDTO;
import com.lucia.memoria.mapper.DeckMapper;
import com.lucia.memoria.model.Deck;
import com.lucia.memoria.model.User;
import com.lucia.memoria.repository.DeckRepository;
import com.lucia.memoria.repository.UserRepository;
import com.lucia.memoria.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public List<DeckDTO> getAllUserDecks(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return deckMapper.toDTO(user.getDecks());
    }
}
