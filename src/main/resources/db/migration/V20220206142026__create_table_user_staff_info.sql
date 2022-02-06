CREATE TABLE user__staff_info(
  user_id int,
  staff_info_id int,

  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (staff_info_id) REFERENCES staff_info(staff_info_id),
  UNIQUE (user_id, staff_info_id)
);