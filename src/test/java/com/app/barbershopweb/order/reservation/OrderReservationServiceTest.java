package com.app.barbershopweb.order.reservation;

import com.app.barbershopweb.order.reservation.repository.OrderReservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

class OrderReservationServiceTest {

    @MockBean
    OrderReservationRepository orderReservationRepository;

    @InjectMocks
    OrderReservationService orderReservationService;

    @Test
    void getBarbershopActiveUnreservedOrdersForWeek() {
    }

    @Test
    void getBarbershopFilteredActiveUnreservedOrdersForWeek() {
    }

    @Test
    void reserveCustomerOrders() {
    }
}