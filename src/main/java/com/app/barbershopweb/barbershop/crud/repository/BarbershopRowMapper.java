package com.app.barbershopweb.barbershop.crud.repository;

import com.app.barbershopweb.barbershop.crud.Barbershop;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BarbershopRowMapper implements RowMapper<Barbershop> {

    @Override
    public Barbershop mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Barbershop(
                rs.getLong("barbershop_id"),
                rs.getString("address"),
                rs.getString("name"),
                rs.getString("phone_number"),
                rs.getString("email"),
                rs.getTime("work_time_from").toLocalTime(),
                rs.getTime("work_time_to").toLocalTime(),
                rs.getBoolean("is_active")
        );
    }
}
