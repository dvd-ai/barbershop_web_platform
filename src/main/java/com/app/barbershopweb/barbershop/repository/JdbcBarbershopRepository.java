package com.app.barbershopweb.barbershop.repository;

import com.app.barbershopweb.barbershop.Barbershop;
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
public class JdbcBarbershopRepository implements BarbershopRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcBarbershopRepository(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Long addBarbershop(Barbershop barbershop) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql =
                "INSERT INTO barbershop(address, name, phone_number, email, work_time_from, work_time_to) " +
                "VALUES (" +
                ":address," +
                ":name," +
                ":phone_number," +
                ":email," +
                ":work_time_from," +
                ":work_time_to" +
                ");";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("address", barbershop.getAddress())
                .addValue("name", barbershop.getName())
                .addValue("phone_number", barbershop.getPhoneNumber())
                .addValue("email", barbershop.getEmail())
                .addValue("work_time_from", barbershop.getWorkTimeFrom())
                .addValue("work_time_to", barbershop.getWorkTimeTo());

        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        return Long.valueOf((Integer) keyHolder.getKeys().get("barbershop_id"));
    }

    @Override
    public Optional<Barbershop> findBarbershopById(Long id) {
        Optional<Barbershop> barbershopOptional;

        String sql =
                "SELECT barbershop_id, address, name, phone_number, email, work_time_from, work_time_to " +
                "FROM barbershop " +
                "WHERE barbershop_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            barbershopOptional = Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new BarbershopRowMapper())
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return barbershopOptional;
    }

    @Override
    public Optional<Barbershop>updateBarbershop(Barbershop barbershop) {
        String sql =
                "UPDATE barbershop " +
                "SET " +
                "address = :address, " +
                "name = :name, " +
                "phone_number = :phone_number, " +
                "email = :email, " +
                "work_time_from = :work_time_from, " +
                "work_time_to = :work_time_to " +
                "WHERE barbershop_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("address", barbershop.getAddress())
                .addValue("name", barbershop.getName())
                .addValue("phone_number", barbershop.getPhoneNumber())
                .addValue(  "email", barbershop.getEmail())
                .addValue(  "id", barbershop.getId())
                .addValue(  "work_time_from", barbershop.getWorkTimeFrom())
                .addValue(  "work_time_to", barbershop.getWorkTimeTo());


        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return findBarbershopById(barbershop.getId());
    }

    @Override
    public List<Barbershop>getBarbershops() {
        String sql =
                "SELECT " +
                "barbershop_id, " +
                "address, " +
                "name," +
                "phone_number," +
                "email, " +
                "work_time_from, " +
                "work_time_to " +
                "FROM barbershop;";


        return namedParameterJdbcTemplate.query(sql, new BarbershopRowMapper());
    }

    @Override
    public void deleteBarbershopById(Long id) {
        String sql = "DELETE FROM barbershop " +
                        "WHERE barbershop_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public boolean barbershopExistsById(Long id) {
        String sql =
                "SELECT COUNT(*) FROM barbershop " +
                "WHERE barbershop_id = :id;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }
}
