package com.app.barbershopweb.order.crud.service;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderService;
import com.app.barbershopweb.order.crud.OrderTestConstants;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

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

    OrderTestConstants otc = new OrderTestConstants();

    @Test
    void addOrder() {
        when(orderRepository.addOrder(any(Order.class)))
                .thenReturn(otc.VALID_ORDER_ID);

        Long orderId = orderService.addOrder(otc.VALID_ORDER_ENTITY);

        assertEquals(otc.VALID_ORDER_ID, orderId);
    }

    @Test
    void updateOrder() {
        when(orderRepository.updateOrder(any(Order.class)))
                .thenReturn(Optional.of(otc.VALID_UPDATED_ORDER_ENTITY));

        Optional<Order> optionalOrder = orderService
                .updateOrder(otc.VALID_UPDATED_ORDER_ENTITY);

        assertTrue(optionalOrder.isPresent());
        assertEquals(otc.VALID_UPDATED_ORDER_ENTITY, optionalOrder.get());
    }

    @Test
    void findOrderByOrderId() {

        when(orderRepository.findOrderByOrderId(any(Long.class)))
                .thenReturn(Optional.of(otc.VALID_ORDER_ENTITY));

        Optional<Order> foundOrder = orderService.findOrderByOrderId(1L);

        assertTrue(foundOrder.isPresent());
        assertEquals(otc.VALID_ORDER_ENTITY, foundOrder.get());
    }

    @Test
    void getOrders() {
        when(orderRepository.getOrders())
                .thenReturn(otc.VALID_ORDER_ENTITY_LIST);

        List<Order> orderList = orderService.getOrders();

        assertEquals(otc.VALID_ORDER_ENTITY_LIST.size(), orderList.size());
        assertEquals(otc.VALID_ORDER_ENTITY_LIST, orderList);
    }
}