CREATE TABLE barbershop(
    barbershop_id serial,
    address char(100) NOT NULL ,
    name char(50) NOT NULL ,
    phone_number char(13) NOT NULL ,
    email char (50) NOT NULL ,

    PRIMARY KEY (barbershop_id)
);