CREATE TABLE workspace
(
    workspace_id  serial,
    user_id       int  NOT NULL,
    barbershop_id int  NOT NULL,
    active        bool NOT NULL,

    PRIMARY KEY (workspace_id),
    UNIQUE (user_id, barbershop_id),

    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (barbershop_id) REFERENCES barbershop (barbershop_id)
)