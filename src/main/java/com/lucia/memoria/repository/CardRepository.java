package com.lucia.memoria.repository;

import com.lucia.memoria.model.Card;
import com.lucia.memoria.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByDeck(Deck deck);
}
