package com.app.barbershopweb.barbershop.repository;

import com.app.barbershopweb.barbershop.Barbershop;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBarbershopRepository implements BarbershopRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcBarbershopRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Long addBarbershop(Barbershop barbershop) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                "insert into barbershop(address, name, phone_number, email) " +
                        "values (" +
                        ":address," +
                        ":name," +
                        ":phone_number," +
                        ":email" +
                        ");",

                new MapSqlParameterSource()
                        .addValue("address", barbershop.getAddress())
                        .addValue("name", barbershop.getName())
                        .addValue("phone_number", barbershop.getPhoneNumber())
                        .addValue("email", barbershop.getEmail()),

                keyHolder
        );
        return (long) (int) keyHolder.getKeys().get("barbershop_id");
    }

    @Override
    public Optional<Barbershop> findBarbershopById(Long id) {
        Optional<Barbershop> barbershopOptional;

        try {
            barbershopOptional = Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(
                            "SELECT barbershop_id, address, name, phone_number, email " +
                                    "FROM barbershop " +
                                    "WHERE barbershop_id = :id;",

                            Map.of("id", id),
                            new BarbershopRowMapper()
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return barbershopOptional;
    }

    @Override
    public Optional<Barbershop>updateBarbershop(Barbershop barbershop) {
        namedParameterJdbcTemplate.update(
                "UPDATE barbershop " +
                        "SET " +
                        "address = :address, " +
                        "name = :name, " +
                        "phone_number = :phone_number, " +
                        "email = :email " +
                        "WHERE barbershop_id = :id;",

                Map.of(
                        "address", barbershop.getAddress(),
                        "name", barbershop.getName(),
                        "phone_number", barbershop.getPhoneNumber(),
                        "email", barbershop.getEmail(),
                        "id", barbershop.getId()
                )
        );
        return findBarbershopById(barbershop.getId());
    }

    @Override
    public List<Barbershop>getBarbershops() {
        return namedParameterJdbcTemplate.query(
                "SELECT " +
                        "barbershop_id, " +
                        "address, " +
                        "name," +
                        "phone_number," +
                        "email " +
                        "FROM barbershop;",

                new BarbershopRowMapper()
        );
    }

    @Override
    public void deleteBarbershopById(Long id) {
        namedParameterJdbcTemplate.update(
                "DELETE FROM barbershop " +
                        "WHERE barbershop_id = :id;",

                Map.of("id", id)
        );
    }
}
