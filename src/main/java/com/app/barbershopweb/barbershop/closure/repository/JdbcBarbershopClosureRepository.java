package com.app.barbershopweb.barbershop.closure.repository;

import com.app.barbershopweb.user.crud.Users;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcBarbershopClosureRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcBarbershopClosureRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Users> getBarbershopVictimCustomers(Long barbershopId) {
        String sql =
                "SELECT users.first_name, users.last_name, users.email " +
                        "FROM users JOIN " +
                        "(SELECT DISTINCT customer_id " +
                        "FROM orders " +
                        "WHERE barbershop_id = :barbershopId AND customer_id IS NOT NULL " +
                        ") AS customers " +
                        "ON users.user_id = customers.customer_id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershopId", barbershopId);

        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, new VictimRowMapper());
    }

}
