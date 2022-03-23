package com.app.barbershopweb.order.crud;

import com.app.barbershopweb.order.crud.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

class OrderServiceTest {

    @MockBean
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void addOrder() {
    }

    @Test
    void deleteOrderByOrderId() {
    }

    @Test
    void updateOrder() {
    }

    @Test
    void findOrderByOrderId() {
    }

    @Test
    void getOrders() {
    }
}