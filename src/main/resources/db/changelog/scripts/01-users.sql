CREATE TABLE users (
        id INT8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
        name VARCHAR(255) UNIQUE NOT NULL,
        CONSTRAINT users_pk PRIMARY KEY (id)
);

INSERT INTO users
(name)
VALUES
('player-one');