package com.app.barbershopweb.user.credentials;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCredentialsRowMapper implements RowMapper<UserCredentials> {

    @Override
    public UserCredentials mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserCredentials(
                rs.getLong("user_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBoolean("enabled")
        );
    }
}
