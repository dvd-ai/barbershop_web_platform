package com.app.barbershopweb.order.reservation.service;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.app.barbershopweb.order.reservation.entity.OrderFilters;
import com.app.barbershopweb.order.reservation.repository.OrderReservationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderReservationServiceTest {

    @Mock
    OrderReservationRepository orderReservationRepository;

    @InjectMocks
    OrderReservationService orderReservationService;

    static List<Order> orders = new ArrayList<>();

    @BeforeAll
    static void init() {
        orders.add(new Order(1L, 1L,
                1L, 1L, LocalDateTime.now(), true));
        orders.add(new Order(2L, 2L,
                2L, 2L, LocalDateTime.now(), true));
        orders.add(new Order(3L, 3L,
                        3L, 3L, LocalDateTime.now(), true));
    }

    @Test
    void getBarbershopActiveUnreservedOrdersForWeek() {
        when(orderReservationRepository
                .getAvailableOrders(any(), any()))
                .thenReturn(orders);

        List<Order> unreservedOrdersForWeek = orderReservationService
                .getBarbershopActiveUnreservedOrdersForWeek(
                        1L, LocalDateTime.now()
                );

        assertEquals(orders.size(), unreservedOrdersForWeek.size());
        assertEquals(orders, unreservedOrdersForWeek);
    }

    @Test
    void getBarbershopFilteredActiveUnreservedOrdersForWeek() {
        when(orderReservationRepository
                .getAvailableFilteredOrders(
                        any(), any(), any()
                )
        )
                .thenReturn(orders);


        List<Order> unreservedFilteredOrdersForWeek = orderReservationService
                .getBarbershopFilteredActiveUnreservedOrdersForWeek(
                        1L, LocalDateTime.now(), new OrderFilters(List.of(1L, 2L))
                );

        verify(orderReservationRepository, times(1)).
                getAvailableFilteredOrders(
                                any(), any(), any()
                        );
        assertEquals(orders.size(), unreservedFilteredOrdersForWeek.size());
        assertEquals(orders, unreservedFilteredOrdersForWeek);
    }

    @Test
    void reserveCustomerOrders() {
        when(orderReservationRepository
                        .reserveOrdersByOrderIdsAndByCustomerId(any(), any())
        )
                .thenReturn(orders);

        List<Order> unreservedFilteredOrdersForWeek = orderReservationService
                .reserveCustomerOrders(
                        List.of(1L, 2L), 1L
                );

        assertEquals(orders.size(), unreservedFilteredOrdersForWeek.size());
        assertEquals(orders, unreservedFilteredOrdersForWeek);
    }
}