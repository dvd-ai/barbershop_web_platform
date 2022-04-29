package com.app.barbershopweb.user.crud.repository;


import com.app.barbershopweb.user.crud.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    public Long addUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql =
                "INSERT INTO users(first_name, last_name, phone_number, email, role, registration_date) " +
                        "VALUES (" +
                        ":first_name," +
                        ":last_name," +
                        ":phone_number," +
                        ":email," +
                        ":role," +
                        ":registration_date" +
                        ");";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("first_name", user.getFirstName())
                .addValue("last_name", user.getLastName())
                .addValue("phone_number", user.getPhoneNumber())
                .addValue("email", user.getEmail())
                .addValue("role", user.getRole())
                .addValue("registration_date", user.getRegistrationDate());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        return Long.valueOf((Integer) keyHolder.getKeys().get("user_id"));
    }


    public Optional<User> findUserById(Long id) {
        Optional<User> usersOptional;

        String sql =
                "SELECT user_id, first_name, last_name, phone_number, email, role, registration_date " +
                        "FROM users " +
                        "WHERE user_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            usersOptional = Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new UserRowMapper())
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return usersOptional;
    }


    public Optional<User> updateUser(User user) {
        String sql =
                "UPDATE users " +
                        "SET " +
                        "first_name = :first_name, " +
                        "last_name = :last_name, " +
                        "phone_number = :phone_number, " +
                        "email = :email, " +
                        "role = :role, " +
                        "registration_date = :registration_date " +
                        "WHERE user_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("first_name", user.getFirstName())
                .addValue("last_name", user.getLastName())
                .addValue("phone_number", user.getPhoneNumber())
                .addValue("email", user.getEmail())
                .addValue("role", user.getRole())
                .addValue("registration_date", user.getRegistrationDate())
                .addValue("id", user.getId());


        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return findUserById(user.getId());
    }


    public List<User> getUsers() {
        String sql =
                "SELECT " +
                        "user_id, " +
                        "first_name, " +
                        "last_name," +
                        "phone_number," +
                        "email, " +
                        "role, " +
                        "registration_date " +
                        "FROM users;";


        return namedParameterJdbcTemplate.query(sql, new UserRowMapper());
    }


    public void deleteUserById(Long id) {
        String sql = "DELETE FROM users " +
                "WHERE user_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }


    public boolean userExistsById(Long id) {
        String sql =
                "SELECT COUNT(*) FROM users " +
                        "WHERE user_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }


    public void truncateAndRestartSequence() {
        String sql = "TRUNCATE users RESTART IDENTITY CASCADE;";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());
    }
}
