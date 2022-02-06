CREATE TABLE user_role(
  user_id int,
  role_id int,

  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (role_id) REFERENCES role(role_id),
  UNIQUE (user_id, role_id)
);