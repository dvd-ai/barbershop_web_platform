CREATE TABLE barbershop(
    barbershop_id serial,
    address varchar(100) NOT NULL ,
    name varchar(50) NOT NULL ,
    phone_number varchar(13) NOT NULL ,
    email varchar (50) NOT NULL ,

    PRIMARY KEY (barbershop_id)
);