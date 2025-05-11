package com.lucia.memoria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @OneToOne
    @JoinColumn(name = "type_id")
    private CardType cardType;

    @OneToOne(mappedBy = "card")
    private Front front;

    @OneToMany(mappedBy = "card")
    private List<Back> backList;

    public Card() {
    }

    public Card(Long cardId, Deck deck, CardType cardType, Front front, List<Back> backList) {
        this.cardId = cardId;
        this.deck = deck;
        this.cardType = cardType;
        this.front = front;
        this.backList = backList;
    }

    public Front getFront() {
        return front;
    }

    public void setFront(Front front) {
        this.front = front;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public List<Back> getBackList() {
        return backList;
    }

    public void setBackList(List<Back> backList) {
        this.backList = backList;
    }
}
