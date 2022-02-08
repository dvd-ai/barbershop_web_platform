CREATE TABLE orders(
    order_id serial,
    barbershop_id int NOT NULL ,
    barber_id int NOT NULL , /*check using java: barber_id != customer_id*/
    customer_id int,
    order_date timestamp NOT NULL ,
    is_active bool NOT NULL,

    PRIMARY KEY (order_id),
    FOREIGN KEY (barbershop_id) REFERENCES barbershop(barbershop_id),
    FOREIGN KEY (barber_id) REFERENCES users(user_id),
    FOREIGN KEY (customer_id) REFERENCES users(user_id),

    UNIQUE (customer_id, order_date),
    UNIQUE (barber_id, order_date)
);