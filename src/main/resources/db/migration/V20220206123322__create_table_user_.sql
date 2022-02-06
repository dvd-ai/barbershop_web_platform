CREATE TABLE users (
    user_id serial,
    first_name char(30) NOT NULL ,
    last_name char(40) NOT NULL ,
    phone_number char(13) NOT NULL ,
    email char(50) NOT NULL ,
    role char(20) NOT NULL,
    registration_date date,

    PRIMARY KEY(user_id)
);

