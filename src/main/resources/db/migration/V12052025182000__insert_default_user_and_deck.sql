INSERT INTO users(username, email, password, created_at)
VALUES('admin', 'admin@email.com', 'admin123', '2025-05-12 20:20:00-00');

INSERT INTO decks(name, user_id)
VALUES ('default', (SELECT user_id FROM users WHERE username = 'admin'));
