package com.app.barbershopweb.order.crud.repository;

import com.app.barbershopweb.barbershop.crud.Barbershop;
import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.InvalidBusinessDataFormatException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final UserRepository userRepository;
    private final BarbershopRepository barbershopRepository;
    private final WorkspaceRepository workspaceRepository;

    public JdbcOrderRepository(DataSource dataSource, UserRepository userRepository,
                               BarbershopRepository barbershopRepository, WorkspaceRepository workspaceRepository) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.userRepository = userRepository;
        this.barbershopRepository = barbershopRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Long addOrder(Order order) {
        checkFkConstraints(order.getBarbershopId(), order.getBarberId(),
                order.getCustomerId());

        checkUkConstraints(order.getBarberId(), order.getCustomerId(),
                order.getOrderDate());
        checkBusinessDataFormat(order);
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
    public Optional<Order> findOrder(Long orderId) {
        Optional<Order> orderOptional;

        String sql =
                "SELECT order_id, barbershop_id, barber_id, customer_id, " +
                        "order_date, is_active " +
                        "FROM orders " +
                        "WHERE order_id = :orderId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("orderId", orderId);

        try {
            orderOptional = Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, new OrderRowMapper())
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
        checkBusinessDataFormat(order);

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
                .addValue("orderDate", order.getOrderDate())
                .addValue("active", order.getActive())
                .addValue("orderId", order.getOrderId());


        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return findOrder(order.getOrderId());
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


        return namedParameterJdbcTemplate.query(sql, new OrderRowMapper());
    }

    @Override
    public void deleteOrder(Long orderId) {
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
    public void deactivateOrdersByBarbershopId(Long barbershopId) {
        String sql =
                "UPDATE orders " +
                        "SET " +
                        "is_active = false " +
                        "WHERE barbershop_id = :barbershopId;";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("barbershopId", barbershopId);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
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

        if (!workspaceRepository.workspaceIsActiveByBarbershopIdAndUserId(barbershopId, barberId))
            messages.add(
                    fkViolation + "barber with id " + barberId +
                            " doesn't work at barbershop with id " + barbershopId
            );

        if (customerId != null && !userRepository.userExistsById(customerId)) {
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

    //this check should be after fk and uk checks
    private void checkBusinessDataFormat(Order order) {
        List<String> messages = new ArrayList<>();
        LocalTime orderTime = order.getOrderDate().toLocalTime();
        Barbershop barbershop = barbershopRepository.findBarbershopById(order.getBarbershopId()).get();


        if (order.getBarberId().equals(order.getCustomerId())) {
            messages.add("Order with id " + order.getOrderId() + " shouldn't have equal customerId and barberId");
        }

        if (orderTime.isAfter(barbershop.getWorkTimeTo()) || orderTime.isBefore(barbershop.getWorkTimeFrom())) {
            messages.add(
                    "orderDate with time " + orderTime + " violates barbershop order hours (" +
                            barbershop.getWorkTimeFrom() + " - " + barbershop.getWorkTimeTo().minusHours(1L) +
                            ")"
            );
        }

        if ((orderTime.getHour() * 3600 + orderTime.getMinute() * 60 + orderTime.getSecond()) % 3600 != 0) {
            messages.add(
                    "orderDate with time " + orderTime + " should be hourly formatted"
            );
        }

        if (!messages.isEmpty()) {
            throw new InvalidBusinessDataFormatException(messages);
        }
    }
}
