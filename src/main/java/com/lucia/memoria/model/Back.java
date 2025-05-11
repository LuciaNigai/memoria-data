package com.lucia.memoria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "back")
public class Back {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "back_id", nullable = false)
    private Long backId;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Back() {
    }

    public Back(Long backId, String content, Card card) {
        this.backId = backId;
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
        return backId;
    }

    public void setFrontId(Long backId) {
        this.backId = backId;
    }

    public Long getBackId() {
        return backId;
    }

    public void setBackId(Long backId) {
        this.backId = backId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
