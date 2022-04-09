package com.app.barbershopweb.order.crud.service;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderService;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_UPDATED_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_VALID_ORDER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;


    @Test
    void addOrder() {
        when(orderRepository.addOrder(any(Order.class)))
                .thenReturn(ORDER_VALID_ORDER_ID);

        Long orderId = orderService.addOrder(ORDER_VALID_ENTITY);

        assertEquals(ORDER_VALID_ORDER_ID, orderId);
    }

    @Test
    void updateOrder() {
        when(orderRepository.updateOrder(any(Order.class)))
                .thenReturn(Optional.of(ORDER_VALID_UPDATED_ENTITY));

        Optional<Order> optionalOrder = orderService
                .updateOrder(ORDER_VALID_UPDATED_ENTITY);

        assertTrue(optionalOrder.isPresent());
        assertEquals(ORDER_VALID_UPDATED_ENTITY, optionalOrder.get());
    }

    @Test
    void findOrder() {

        when(orderRepository.findOrder(any(Long.class)))
                .thenReturn(Optional.of(ORDER_VALID_ENTITY));

        Optional<Order> foundOrder = orderService.findOrder(1L);

        assertTrue(foundOrder.isPresent());
        assertEquals(ORDER_VALID_ENTITY, foundOrder.get());
    }

    @Test
    void getOrders() {
        when(orderRepository.getOrders())
                .thenReturn(ORDER_VALID_ENTITY_LIST);

        List<Order> orderList = orderService.getOrders();

        assertEquals(ORDER_VALID_ENTITY_LIST.size(), orderList.size());
        assertEquals(ORDER_VALID_ENTITY_LIST, orderList);
    }

    @Test
    void deleteOrder() {
        orderService.deleteOrder(ORDER_VALID_ORDER_ID);
        verify(orderRepository, times(1)).deleteOrder(ORDER_VALID_ORDER_ID);
    }
}