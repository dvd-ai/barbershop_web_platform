package com.app.barbershopweb.barbershop.registration.repository.jdbc;

import com.app.barbershopweb.barbershop.Barbershop;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;

@Repository
public class JdbcBarbershopRegistrationRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcBarbershopRegistrationRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Long save(Barbershop barbershop) {
        String sql = "insert into barbershop(address, name, phone_number, email) " +
                "values (" +
                    ":address," +
                    ":name," +
                    ":phone_number," +
                    ":email" +
                ");";

        Map<String, String> namedParameters = Map.of(
                "address", barbershop.getAddress(),
                "name", barbershop.getName(),
                "phone_number", barbershop.getPhoneNumber(),
                "email", barbershop.getEmail()
        );

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, (SqlParameterSource) namedParameters, keyHolder);
        return (Long) keyHolder.getKeys().get("id");
    }
}
