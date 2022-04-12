CREATE TABLE users
(
    user_id           serial,
    first_name        varchar(30) NOT NULL,
    last_name         varchar(40) NOT NULL,
    phone_number      varchar(13) NOT NULL,
    email             varchar(50) NOT NULL,
    role              varchar(20) NOT NULL,
    registration_date timestamp,

    PRIMARY KEY (user_id)
);

