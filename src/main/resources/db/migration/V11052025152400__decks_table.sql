CREATE TABLE IF NOT EXISTS decks(
    deck_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    parent_deck_id INTEGER,
    user_id INTEGER NOT NULL,
    CONSTRAINT deck_parent_deck_fk FOREIGN KEY(parent_deck_id) REFERENCES decks(deck_id) ON DELETE CASCADE,
    CONSTRAINT deck_user_fk FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT unique_name_per_user_deck UNIQUE(name, user_id, parent_deck_id)
);