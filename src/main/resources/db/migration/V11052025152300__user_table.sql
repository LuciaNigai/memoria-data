CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    username varchar(200) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password varchar(250) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    last_login TIMESTAMP
);