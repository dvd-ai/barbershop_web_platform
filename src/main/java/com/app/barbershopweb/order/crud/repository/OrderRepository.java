package com.app.barbershopweb.order.crud.repository;

import com.app.barbershopweb.order.crud.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository {

    Long addOrder(Order order);

    Optional<Order> findOrder(Long orderId);

    Optional<Order> updateOrder(Order order);

    List<Order> getOrders();

    void deleteOrder(Long orderId);

    boolean orderExistsByOrderId(Long orderId);

    boolean orderExistsByCustomerIdAndOrderDate(Long customerId, LocalDateTime orderDate);

    boolean orderExistsByBarberIdAndOrderDate(Long barberId, LocalDateTime orderDate);

    void deactivateOrdersByBarbershopId(Long barbershopId);

    void truncateAndRestartSequence();

}
