package com.app.barbershopweb.order.reservation.repository;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcOrderReservationRepository implements OrderReservationRepository{

    private final OrderRepository orderRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcOrderReservationRepository(OrderRepository orderRepository, DataSource dataSource) {
        this.orderRepository = orderRepository;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Order> getActiveUnreservedOrdersByBarbershopIdAndWeek(
            Long barbershopId, LocalDateTime week
    ) {
        return null;
    }

    @Override
    public List<Order> getActiveUnreservedOrdersByBarbershopIdAndWeekAndBarberIds(
            Long barbershopId, LocalDateTime week, List<Long> barberIds
    ) {
        return null;
    }

    @Override
    public Order reserveOrderByCustomerId(Long orderId, Long customerId) {
        return null;
    }

    @Override
    public List<Order> reserveOrdersByCustomerId(List<Long> orderIds, Long customerId) {
        return null;
    }
}
