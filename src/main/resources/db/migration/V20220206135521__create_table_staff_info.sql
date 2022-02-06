CREATE TABLE staff_info(
  staff_info_id serial,
  job_position_name char(30) NOT NULL ,
  barbershop_id int NOT NULL ,
  active bool NOT NULL ,
  hour_experience int,

  PRIMARY KEY (staff_info_id),
  FOREIGN KEY (barbershop_id) REFERENCES barbershop(barbershop_id)
);