package com.app.barbershopweb.user.repository;


import com.app.barbershopweb.user.Users;
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
public class JdbcUsersRepository implements UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcUsersRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Long addUser(Users users) {
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
                .addValue("first_name", users.getFirstName())
                .addValue("last_name", users.getLastName())
                .addValue("phone_number", users.getPhoneNumber())
                .addValue("email", users.getEmail())
                .addValue("role", users.getRole())
                .addValue("registration_date", users.getRegistrationDate());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        return Long.valueOf((Integer) keyHolder.getKeys().get("user_id"));
    }

    @Override
    public Optional<Users> findUserById(Long id) {
        Optional<Users> usersOptional;

        String sql =
                "SELECT user_id, first_name, last_name, phone_number, email, role, registration_date " +
                        "FROM users " +
                        "WHERE user_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            usersOptional = Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new UsersRowMapper())
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return usersOptional;
    }

    @Override
    public Optional<Users> updateUser(Users users) {
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
                .addValue("first_name", users.getFirstName())
                .addValue("last_name", users.getLastName())
                .addValue("phone_number", users.getPhoneNumber())
                .addValue(  "email", users.getEmail())
                .addValue(  "role", users.getRole())
                .addValue(  "registration_date", users.getRegistrationDate())
                .addValue(  "id", users.getId());


        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return findUserById(users.getId());
    }

    @Override
    public List<Users> getUsers() {
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


        return namedParameterJdbcTemplate.query(sql, new UsersRowMapper());
    }

    @Override
    public void deleteUserById(Long id) {
        String sql = "DELETE FROM users " +
                "WHERE user_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public boolean userExistsById(Long id) {
        String sql =
                "SELECT COUNT(*) FROM users " +
                        "WHERE user_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }

    @Override
    public void truncateAndRestartIdentity() {
        String sql = "TRUNCATE users RESTART IDENTITY CASCADE;";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());
    }
}
