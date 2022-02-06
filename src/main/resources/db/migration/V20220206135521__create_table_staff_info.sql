CREATE TABLE staff_info(
  staff_info_id serial,
  job_position_id int,
  barbershop_id int,
  active bool,
  hour_experience int,

  PRIMARY KEY (staff_info_id),
  FOREIGN KEY (job_position_id) REFERENCES job_position(job_position_id),
  FOREIGN KEY (barbershop_id) REFERENCES barbershop(barbershop_id)
);