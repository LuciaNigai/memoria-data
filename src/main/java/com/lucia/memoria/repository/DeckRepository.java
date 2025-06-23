package com.lucia.memoria.repository;

import com.lucia.memoria.model.Deck;
import com.lucia.memoria.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DeckRepository extends JpaRepository<Deck, Long> {

  Optional<Deck> findByPathAndUser(String path, User user);

  Optional<Deck> findByPath(String path);

  List<Deck> findAllByUser(User user);

  Optional<Deck> findByDeckId(UUID deckId);
}
