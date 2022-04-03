package com.app.barbershopweb.order.reservation.service;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.app.barbershopweb.order.reservation.OrderReservationTestConstants;
import com.app.barbershopweb.order.reservation.entity.OrderFilters;
import com.app.barbershopweb.order.reservation.repository.OrderReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    OrderReservationTestConstants ortc = new OrderReservationTestConstants();

    @Test
    void getAvailableOrders() {
        when(orderReservationRepository
                .getAvailableOrders(any(), any()))
                .thenReturn(ortc.UNRESERVED_ORDER_ENTITY_LIST);

        List<Order> unreservedOrdersForWeek = orderReservationService
                .getAvailableOrders(
                        ortc.SUOR_DTO_NO_FILTERS.barbershopId(),
                        ortc.SUOR_DTO_NO_FILTERS.startWeekDate()
                );

        assertEquals(ortc.UNRESERVED_ORDER_ENTITY_LIST.size(), unreservedOrdersForWeek.size());
        assertEquals(ortc.UNRESERVED_ORDER_ENTITY_LIST, unreservedOrdersForWeek);
    }

    @Test
    @DisplayName("when order filters (barberIds) were specified returns available filtered orders")
    void getAvailableFilteredOrders() {
        when(orderReservationRepository
                .getAvailableFilteredOrders(
                        any(), any(), any()
                )
        )
                .thenReturn(ortc.UNRESERVED_FILTERED_ORDER_ENTITY_LIST);


        List<Order> unreservedFilteredOrdersForWeek = orderReservationService
                .getFilteredAvailableOrders(
                        ortc.SUOR_DTO_WITH_FILTERS.barbershopId(),
                        ortc.SUOR_DTO_WITH_FILTERS.startWeekDate(),
                        ortc.SUOR_DTO_WITH_FILTERS.orderFilters()
                );

        verify(orderReservationRepository, times(0)).
                getAvailableOrders(
                                any(), any()
                        );
        assertEquals(ortc.UNRESERVED_FILTERED_ORDER_ENTITY_LIST.size(), unreservedFilteredOrdersForWeek.size());
        assertEquals(ortc.UNRESERVED_FILTERED_ORDER_ENTITY_LIST, unreservedFilteredOrdersForWeek);
    }

    @Test
    @DisplayName("when order filters (barberIds) were NOT specified returns all available orders")
    void getAvailableFilteredOrdersWithNoFilters() {
        when(orderReservationRepository
                .getAvailableOrders(
                        any(), any()
                )
        )
                .thenReturn(ortc.UNRESERVED_ORDER_ENTITY_LIST);


        List<Order> unreservedFilteredOrdersForWeek = orderReservationService
                .getFilteredAvailableOrders(
                        ortc.SUOR_DTO_NO_FILTERS.barbershopId(),
                        ortc.SUOR_DTO_NO_FILTERS.startWeekDate(),
                        new OrderFilters(List.of())
                );

        verify(orderReservationRepository, times(0)).
                getAvailableFilteredOrders(
                        any(), any(), any()
                );
        assertEquals(ortc.UNRESERVED_ORDER_ENTITY_LIST.size(), unreservedFilteredOrdersForWeek.size());
        assertEquals(ortc.UNRESERVED_ORDER_ENTITY_LIST, unreservedFilteredOrdersForWeek);
    }

    @Test
    void reserveCustomerOrders() {
        when(orderReservationRepository
                        .reserveOrdersByOrderIdsAndByCustomerId(any(), any())
        )
                .thenReturn(ortc.RESERVED_ORDER_ENTITY_LIST);

        List<Order> reservedFilteredOrdersForWeek = orderReservationService
                .reserveCustomerOrders(
                        ortc.VALID_ORDER_RESERV_DTO.orderIds(),
                        ortc.VALID_ORDER_RESERV_DTO.customerId()
                );

        assertEquals(ortc.RESERVED_ORDER_ENTITY_LIST.size(), reservedFilteredOrdersForWeek.size());
        assertEquals(ortc.RESERVED_ORDER_ENTITY_LIST, reservedFilteredOrdersForWeek);
    }
}