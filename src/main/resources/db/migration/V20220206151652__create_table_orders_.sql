CREATE TABLE orders(
    order_id serial,
    registration_date timestamp NOT NULL ,
    implementation_date timestamp NOT NULL ,
    barber_customer_slot_id int NOT NULL ,
    status bool NOT NULL ,

    PRIMARY KEY (order_id),
    UNIQUE(barber_customer_slot_id),
    FOREIGN KEY (barber_customer_slot_id) REFERENCES barber_customer_slot(barber_customer_slot_id)
);