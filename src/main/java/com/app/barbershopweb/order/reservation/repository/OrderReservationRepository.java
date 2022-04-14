package com.app.barbershopweb.order.reservation.repository;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.order.crud.repository.OrderRowMapper;
import com.app.barbershopweb.user.crud.repository.UserRepository;
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
public class OrderReservationRepository {

    private final OrderRepository orderRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UserRepository userRepository;

    public OrderReservationRepository(OrderRepository orderRepository, DataSource dataSource, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.userRepository = userRepository;
    }

    public List<Order> getAvailableOrders(
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

    public List<Order> getAvailableFilteredOrders(
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


    private Optional<Order> reserveOrderByOrderIdAndCustomerId(Long orderId, Long customerId) {
        checkOrderUk(orderId, customerId);

        String sql =
                "UPDATE orders " +
                        "SET customer_id = :customerId " +
                        "WHERE order_id = :orderId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("customerId", customerId)
                .addValue("orderId", orderId);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return orderRepository.findOrder(orderId);
    }

    private void checkOrderUk(Long orderId, Long customerId) {
        Order order = orderRepository.findOrder(orderId).get();

        if (orderRepository.orderExistsByCustomerIdAndOrderDate(
                customerId, order.getOrderDate())
        ) {
            throw new DbUniqueConstraintsViolationException(
                    List.of(
                            "uk violation during order reservation (" +
                                    "orderId " + order.getOrderId() + ") :" +
                                    " order with" +
                                    " customerId " + customerId +
                                    " and orderDate " + order.getOrderDate() +
                                    " already exists"
                    )
            );
        }
    }

    public List<Order> reserveOrders(List<Long> orderIds, Long customerId) {
        checkCustomerExistence(customerId);
        checkOrdersExistence(orderIds);

        return orderIds.stream()
                .map(orderId -> reserveOrderByOrderIdAndCustomerId(orderId, customerId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private void checkCustomerExistence(Long customerId) {
        if (!userRepository.userExistsById(customerId))
            throw new NotFoundException(
                    List.of(
                            "Customer with id " + customerId +
                                    " wasn't found during order reservation"
                    )
            );
    }

    private void checkOrdersExistence(List<Long> orderIds) {
        List<String> messages = new ArrayList<>();

        orderIds.stream()
                .filter(orderId -> !orderRepository.orderExistsByOrderId(orderId))
                .forEach(oId -> messages.add("Order with id " + oId + " wasn't found during order reservation"));

        if (!messages.isEmpty()) {
            throw new NotFoundException(messages);
        }

    }

}
