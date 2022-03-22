package com.app.barbershopweb.order.reservation.repository;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.order.crud.repository.OrderRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcOrderReservationRepository implements OrderReservationRepository{

    private final OrderRepository orderRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcOrderReservationRepository(OrderRepository orderRepository, DataSource dataSource) {
        this.orderRepository = orderRepository;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Order> getActiveUnreservedOrdersForWeekByBarbershopIdAndDate(
            Long barbershopId, LocalDateTime dateToStartWeekFrom
    ) {
        String sql =
                "SELECT " +
                    "order_id, " +
                    "barbershop_id, " +
                    "barber_id, " +
                    "customer_id, " +
                    "order_date, " +
                    "is_active " +
                "FROM orders " +
                "WHERE is_active = true " +
                    "AND barbershop_id = :barbershopId " +
                    "AND customer_id IS NULL " +
                    "AND (order_date BETWEEN" +
                        " :dateToStartWeekFrom AND ( :dateToStartWeekFrom + INTERVAL '7 DAYS' )" +
                        ");";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershopId", barbershopId)
                .addValue("dateToStartWeekFrom", dateToStartWeekFrom);

        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, new OrderRowMapper());
    }

    @Override
    public List<Order> getActiveUnreservedOrdersForWeekByBarbershopIdAndDateAndBarberIds(
            Long barbershopId, LocalDateTime dateToStartWeekFrom, List<Long> barberIds
    ) {
        String sql =
                "SELECT " +
                        "order_id, " +
                        "barbershop_id, " +
                        "barber_id, " +
                        "customer_id, " +
                        "order_date, " +
                        "is_active " +
                        "FROM orders " +
                        "WHERE is_active = true " +
                            "AND barbershop_id = :barbershopId " +
                            "AND customer_id IS NULL " +
                            "AND barber_id IN ( :barberIds)" +
                            "AND (order_date BETWEEN" +
                            " :dateToStartWeekFrom AND ( :dateToStartWeekFrom + INTERVAL '7 DAYS' )" +
                            ");";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershopId", barbershopId)
                .addValue("barberIds", barberIds)
                .addValue("dateToStartWeekFrom", dateToStartWeekFrom);

        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, new OrderRowMapper());
    }

    @Override
    public Optional<Order> reserveOrderByOrderIdAndCustomerId(Long orderId, Long customerId) {
        String sql =
                "UPDATE orders " +
                    "SET customer_id = :customerId " +
                    "WHERE order_id = :orderId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("customerId", customerId)
                .addValue("orderId", orderId);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return orderRepository.findOrderByOrderId(orderId);
    }

    @Override
    public List<Optional<Order>> reserveOrdersByOrderIdsAndByCustomerId(List<Long> orderIds, Long customerId) {
        List<Optional<Order>> orderOptionals = new ArrayList<>();
        orderIds.forEach(
                order -> orderOptionals.add(reserveOrderByOrderIdAndCustomerId(order, customerId))
        );
        return orderOptionals;
    }
}
