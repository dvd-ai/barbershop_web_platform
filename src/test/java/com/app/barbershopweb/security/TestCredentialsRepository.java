package com.app.barbershopweb.security;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.time.LocalDateTime;

public class TestCredentialsRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public final static String USER_USERNAME = "user-username";
    public final static String PASSWORD = "$2a$12$3FhSbjPqQBMWvmNmV/DmDOOwt9XzZRan53Vh1P/OmTJd5lE09Jwcm";


    public TestCredentialsRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void createUserPrincipal() {
        createUserPrincipalCredentials(createUser());
    }

    public void removeAllPrincipals() {
        String sql = "TRUNCATE users, user_credentials RESTART IDENTITY CASCADE;";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());
    }

    private Long createUser() {
            String sql =
            "INSERT INTO users(user_id, first_name, last_name, phone_number, email, role, registration_date) " +
            "VALUES (" +
             1000 + ", " +
            "'first_name', " +
            "'last_name'," +
            "'phone_number'," +
            "'email'," +
            "'USER'," +
            ":registration_date" +
            ");";

    SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
            .addValue("registration_date", LocalDateTime.now());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return 1000L;
    }

    private void createUserPrincipalCredentials(Long userId) {
        String sql =
                "INSERT INTO user_credentials(user_id, username, password, enabled) " +
                        "VALUES (" +
                        ":userId," +
                        ":username," +
                        ":password," +
                        "true" +
                        ");";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("username", USER_USERNAME)
                .addValue("password", PASSWORD);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

}
