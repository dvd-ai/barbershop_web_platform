CREATE TABLE time_slot(
    slot_id serial,
    day char(15) NOT NULL ,
    hour char(2) NOT NULL ,

    PRIMARY KEY (slot_id)
);