CREATE TABLE users_barbershop (
    users_barbershop serial,
    user_id int NOT NULL ,
    barbershop_id int NOT NULL ,
    active bool NOT NULL ,

    PRIMARY KEY (users_barbershop),
    UNIQUE (user_id, barbershop_id),

    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (barbershop_id) REFERENCES barbershop(barbershop_id)
)