CREATE TABLE barber_customer_slot(
    barber_customer_slot_id serial,
    slot_id int,
    barbershop_id int,
    barber_id int CHECK ( barber_id != customer_id ),
    customer_id int,

    PRIMARY KEY (barber_customer_slot_id),
    FOREIGN KEY (barbershop_id) REFERENCES barbershop(barbershop_id),
    FOREIGN KEY (barber_id) REFERENCES user_(user_id),
    FOREIGN KEY (customer_id) REFERENCES user_(user_id),
    FOREIGN KEY (slot_id) REFERENCES time_slot(slot_id),

    UNIQUE (customer_id, slot_id),
    UNIQUE (barber_id, slot_id)
);