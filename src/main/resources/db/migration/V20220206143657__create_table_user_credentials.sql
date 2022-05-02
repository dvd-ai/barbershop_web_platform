CREATE TABLE user_credentials (
    user_id           int,
    username varchar(50) NOT NULL,
    password varchar(80) NOT NULL,
    enabled bool NOT NULL,

    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    UNIQUE (username)
);

