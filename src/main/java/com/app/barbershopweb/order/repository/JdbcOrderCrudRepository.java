package com.app.barbershopweb.order.repository;

import com.app.barbershopweb.barbershop.repository.BarbershopRepository;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.Order;
import com.app.barbershopweb.user.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcOrderCrudRepository implements OrderCrudRepository{
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UserRepository userRepository;
    private final BarbershopRepository barbershopRepository;


    public JdbcOrderCrudRepository(DataSource dataSource, UserRepository userRepository,
                                   BarbershopRepository barbershopRepository) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.userRepository = userRepository;
        this.barbershopRepository = barbershopRepository;
    }

    @Override
    public Long addOrder(Order order) {
        checkFkConstraints(order.getBarbershopId(), order.getBarberId(), 
                order.getCustomerId());
        
        checkUkConstraints(order.getBarberId(), order.getCustomerId(),
                order.getOrderDate());
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        String sql =
                "INSERT INTO orders(barbershop_id, barber_id," +
                        " customer_id, order_date, is_active) " +
                        "VALUES (" +
                        ":barbershopId," +
                        ":barberId," +
                        ":customerId," +
                        ":orderDate," +
                        ":active" +
                        ");";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershopId", order.getBarbershopId())
                .addValue("barberId", order.getBarberId())
                .addValue("customerId", order.getCustomerId())
                .addValue("orderDate", order.getOrderDate())
                .addValue("active", order.getActive());
        
        namedParameterJdbcTemplate.update(sql, sqlParameterSource, keyHolder);
        return Long.valueOf((Integer) keyHolder.getKeys().get("order_id"));
    }

    @Override
    public Optional<Order> findOrderByOrderId(Long orderId) {
        Optional<Order> orderOptional;

        String sql =
                "SELECT order_id, barbershop_id, barber_id, customer_id, " +
                        "order_date, is_active " +
                        "FROM orders " +
                        "WHERE order_id = :orderId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", orderId);

        try {
            orderOptional = Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new OrderCrudRowMapper())
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return orderOptional;
    }

    @Override
    public Optional<Order> updateOrder(Order order) {
        checkFkConstraints(order.getBarbershopId(), order.getBarberId(),
                order.getCustomerId());
        checkUkConstraints(order.getBarberId(), order.getCustomerId(),
                order.getOrderDate());

        String sql =
                "UPDATE orders " +
                        "SET " +
                        "barber_id = :barberId, " +
                        "barbershop_id = :barbershopId, " +
                        "customer_id = :customerId, " +
                        "order_date = :orderDate, " +
                        "is_active = :active " +
                        "WHERE order_id = :orderId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barberId", order.getBarberId())
                .addValue("barbershopId", order.getBarbershopId())
                .addValue("customerId", order.getCustomerId())
                .addValue(  "orderDate", order.getOrderDate())
                .addValue(  "active", order.getActive());


        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return findOrderByOrderId(order.getOrderId());
    }

    @Override
    public List<Order> getOrders() {
        String sql =
                "SELECT " +
                        "order_id, " +
                        "barbershop_id, " +
                        "barber_id," +
                        "customer_id," +
                        "order_date, " +
                        "is_active " +
                        "FROM orders;";


        return namedParameterJdbcTemplate.query(sql, new OrderCrudRowMapper());
    }

    @Override
    public void deleteOrderByOrderId(Long orderId) {
        String sql = "DELETE FROM orders " +
                "WHERE order_id = :orderId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("orderId", orderId);

        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public boolean orderExistsByOrderId(Long orderId) {
        String sql =
                "SELECT COUNT(*) FROM orders " +
                "WHERE order_id = :orderId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("orderId", orderId);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }

    @Override
    public boolean orderExistsByCustomerIdAndOrderDate(Long customerId, 
                                                       LocalDateTime orderDate) {
        String sql =
                "SELECT COUNT(*) FROM orders " +
                "WHERE customer_id = :customerId " +
                "AND order_date = :orderDate;";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customerId", customerId)
                .addValue("orderDate", orderDate);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, parameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }

    @Override
    public boolean orderExistsByBarberIdAndOrderDate(Long barberId,
                                                     LocalDateTime orderDate) {
        String sql =
                "SELECT COUNT(*) FROM orders " +
                        "WHERE barber_id = :barberId " +
                        "AND order_date = :orderDate;";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("barberId", barberId)
                .addValue("orderDate", orderDate);

        Integer count = namedParameterJdbcTemplate.queryForObject(sql, parameterSource, Integer.class);
        return Objects.requireNonNull(count) > 0;
    }

    @Override
    public void truncateAndRestartSequence() {
        String sql = "TRUNCATE orders RESTART IDENTITY;";
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource());
    }

    private void checkFkConstraints(Long barbershopId, Long barberId, Long customerId) {
        String fkViolation = "fk violation: ";
        String notPresent = " not present";
        List<String> messages = new ArrayList<>();

        if (!barbershopRepository.barbershopExistsById(barbershopId)) {
            messages.add(fkViolation + "barbershop with id " + barbershopId + notPresent);
        }

        if (!userRepository.userExistsById(barberId)) {
            messages.add(fkViolation + "barber with id " + barberId + notPresent);
        }

        if (!userRepository.userExistsById(customerId)) {
            messages.add(fkViolation + "customer with id " + customerId + notPresent);
        }

        if (!messages.isEmpty()) {
            throw new NotFoundException(messages);
        }
    }

    private void checkUkConstraints(Long barberId, Long customerId, LocalDateTime orderDate) {
        List<String> messages = new ArrayList<>();
        String ukViolation = "uk violation: order with ";
        String exists = " already exists";

        if (orderExistsByBarberIdAndOrderDate(barberId, orderDate)) {
            messages.add(ukViolation + "barberId " + barberId +
                    " and orderDate " + orderDate + exists);
        }

        if (orderExistsByCustomerIdAndOrderDate(customerId, orderDate)) {
            messages.add(ukViolation + "customerId " + customerId +
                    " and orderDate " + orderDate + exists);
        }

        if (!messages.isEmpty()) {
            throw new DbUniqueConstraintsViolationException(messages);
        }
    }
}
