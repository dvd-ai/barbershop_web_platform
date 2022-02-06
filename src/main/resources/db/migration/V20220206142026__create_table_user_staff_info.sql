CREATE TABLE users_staff_info(
  user_id int NOT NULL ,
  staff_info_id int NOT NULL ,

  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (staff_info_id) REFERENCES staff_info(staff_info_id),
  UNIQUE (user_id, staff_info_id)
);