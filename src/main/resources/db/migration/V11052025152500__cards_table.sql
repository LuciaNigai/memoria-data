CREATE TABLE IF NOT EXISTS cards(
    card_id BIGSERIAL PRIMARY KEY,
    deck_id BIGINT NOT NULL,
    type_id BIGINT NOT NULL,
    CONSTRAINT card_deck_fk FOREIGN KEY(deck_id) REFERENCES decks(deck_id) ON DELETE CASCADE,
    CONSTRAINT card_type_fk FOREIGN KEY(type_id) REFERENCES card_types(type_id) ON DELETE RESTRICT
);