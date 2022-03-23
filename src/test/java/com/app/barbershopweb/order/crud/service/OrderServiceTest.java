package com.app.barbershopweb.order.crud.service;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderService;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void addOrder() {
        Order order = new Order(1L, 1L,
                1L, 1L, LocalDateTime.now(), true);

        when(orderRepository.addOrder(any(Order.class)))
                .thenReturn(order.getOrderId());

        Long orderId = orderService.addOrder(order);

        assertEquals(order.getOrderId(), orderId);
    }

    @Test
    void updateOrder() {
        Order orderToUpdate = new Order(1L, 3L,
                10L, 1L, LocalDateTime.now(), true);

        when(orderRepository.updateOrder(any(Order.class)))
                .thenReturn(Optional.of(orderToUpdate));

        Optional<Order> optionalOrder = orderService.updateOrder(orderToUpdate);

        assertTrue(optionalOrder.isPresent());
        assertEquals(orderToUpdate, optionalOrder.get());
    }

    @Test
    void findOrderByOrderId() {
        Order order = new Order(1L, 1L,
                1L, 1L, LocalDateTime.now(), true);

        when(orderRepository.findOrderByOrderId(any(Long.class)))
                .thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderService.findOrderByOrderId(1L);

        assertTrue(foundOrder.isPresent());
        assertEquals(order, foundOrder.get());
    }

    @Test
    void getOrders() {
        List<Order> orders = List.of(
                new Order(1L, 1L,
                        1L, 1L, LocalDateTime.now(), true),
                new Order(2L, 2L,
                        2L, 2L, LocalDateTime.now(), true)
        );

        when(orderRepository.getOrders())
                .thenReturn(orders);

        List<Order> orderList = orderService.getOrders();

        assertEquals(orders.size(), orderList.size());
        assertEquals(orders, orderList);
    }
}