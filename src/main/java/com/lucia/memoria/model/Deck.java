package com.lucia.memoria.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.List;

@Entity
@Table(name = "decks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "user_id", "parent_deck_id"})
})
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deck_id")
    private Long deckId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_deck_id")
    private Deck parentDeck;

    @OneToMany(mappedBy = "parentDeck")
    private List<Deck> childDecks;

    @OneToMany(mappedBy = "deck")
    private List<Card> cards;

    public Deck() {
    }

    public Deck(Long deckId, User user, String name, Deck parentDeck, List<Deck> childDecks, List<Card> cards) {
        this.deckId = deckId;
        this.user = user;
        this.name = name;
        this.parentDeck = parentDeck;
        this.childDecks = childDecks;
        this.cards = cards;
    }

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Deck getParentDeck() {
        return parentDeck;
    }

    public void setParentDeck(Deck parentDeck) {
        this.parentDeck = parentDeck;
    }

    public List<Deck> getChildDecks() {
        return childDecks;
    }

    public void setChildDecks(List<Deck> childDecks) {
        this.childDecks = childDecks;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
