package com.lucia.memoria.repository;

import com.lucia.memoria.model.Card;
import com.lucia.memoria.model.Deck;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<Card, Long> {

  List<Card> findAllByDeck(Deck deck);

  @Query("SELECT c FROM Card c " +
      "LEFT JOIN FETCH c.fields f " +
      "LEFT JOIN FETCH c.template t " +
      "LEFT JOIN FETCH t.fields tf " +
      "WHERE c.cardId = :cardId")
  Optional<Card> findByCardIdWithFieldsAndTemplateFields(@Param("cardId") UUID cardId);
}
