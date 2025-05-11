package com.lucia.memoria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "front")
public class Front {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "front_id", nullable = false)
    private Long frontId;

    @Column(name = "content")
    private String content;

    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Front() {
    }

    public Front(Long frontId, String content, Card card) {
        this.frontId = frontId;
        this.content = content;
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Long getFrontId() {
        return frontId;
    }

    public void setFrontId(Long frontId) {
        this.frontId = frontId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
