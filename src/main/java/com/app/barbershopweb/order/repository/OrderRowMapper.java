package com.app.barbershopweb.order.repository;

import com.app.barbershopweb.order.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Order(
          rs.getLong("order_id"),
          rs.getLong("barbershop_id"),
          rs.getLong("barber_id"),
          rs.getLong("customer_id"),
          rs.getTimestamp("order_date").toLocalDateTime(),
          rs.getBoolean("is_active")
        );
    }
}