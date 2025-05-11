CREATE TABLE IF NOT EXISTS cards(
    card_id SERIAL PRIMARY KEY,
    deck_id INTEGER NOT NULL,
    type_id INTEGER NOT NULL,
    CONSTRAINT card_deck_fk FOREIGN KEY(deck_id) REFERENCES decks(deck_id) ON DELETE CASCADE,
    CONSTRAINT card_type_fk FOREIGN KEY(type_id) REFERENCES card_types(type_id) ON DELETE RESTRICT
);