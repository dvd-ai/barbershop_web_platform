package com.app.barbershopweb.order.repository;

import com.app.barbershopweb.order.Order;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderCrudRepository {

    Long addOrder(Order order);
    Optional<Order> findOrderByOrderId(Long id);
    Optional<Order>updateOrder(Order order);
    List<Order> getOrders();
    void deleteOrderByOrderId(Long orderId);
    boolean orderExistsByOrderId(Long orderId);
    boolean orderExistsByCustomerIdAndOrderDate(Long customerId, LocalDateTime orderDate);
    boolean orderExistsByBarberIdAndOrderDate(Long barberId, LocalDateTime orderDate);
    void truncateAndRestartSequence();

}
