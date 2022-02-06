CREATE TABLE order_(
    order_id serial,
    registration_date date,
    implementation_date date,
    barber_customer_slot_id int,
    status bool,

    PRIMARY KEY (order_id),
    UNIQUE(barber_customer_slot_id),
    FOREIGN KEY (barber_customer_slot_id) REFERENCES barber_customer_slot(barber_customer_slot_id)
);