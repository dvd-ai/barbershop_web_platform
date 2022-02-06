CREATE TABLE barber_customer_slot(
    barber_customer_slot_id serial,
    slot_id int NOT NULL ,
    barbershop_id int NOT NULL ,
    barber_id int CHECK ( barber_id != customer_id ) NOT NULL ,
    customer_id int NOT NULL ,

    PRIMARY KEY (barber_customer_slot_id),
    FOREIGN KEY (barbershop_id) REFERENCES barbershop(barbershop_id),
    FOREIGN KEY (barber_id) REFERENCES users(user_id),
    FOREIGN KEY (customer_id) REFERENCES users(user_id),
    FOREIGN KEY (slot_id) REFERENCES time_slot(slot_id),

    UNIQUE (customer_id, slot_id),
    UNIQUE (barber_id, slot_id)
);