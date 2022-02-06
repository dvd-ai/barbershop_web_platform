CREATE TABLE users (
    user_id serial,
    first_name char(30),
    last_name char(40),
    phone_number char(13),
    email char(50),
    registration_date date,

    PRIMARY KEY(user_id)
);

