package com.app.barbershopweb.order.reservation.service;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.app.barbershopweb.order.reservation.entity.OrderFilters;
import com.app.barbershopweb.order.reservation.repository.OrderReservationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Dto__TestConstants.GET_OPEN_ORDERS__REQUEST_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Filtered_Dto__TestConstants.GET_OPEN_FILTERED_ORDERS__REQUEST_DTO;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderReservationServiceTest {

    @Mock
    OrderReservationRepository orderReservationRepository;

    @InjectMocks
    OrderReservationService orderReservationService;


    @Test
    void getAvailableOrders() {
        when(orderReservationRepository
                .getAvailableOrders(
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate()
                )
        )
                .thenReturn(ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST);

        List<Order> unreservedOrdersForWeek = orderReservationService
                .getAvailableOrders(
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate()
                );

        assertEquals(ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST.size(), unreservedOrdersForWeek.size());
        assertEquals(ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST, unreservedOrdersForWeek);
    }

    @Test
    @DisplayName("when order filters (barberIds) were specified returns available filtered orders")
    void getAvailableFilteredOrders() {
        when(orderReservationRepository
                .getAvailableFilteredOrders(
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.barbershopId(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.startWeekDate(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.orderFilters().getBarberIds()
                )
        )
                .thenReturn(ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST);


        List<Order> unreservedFilteredOrdersForWeek = orderReservationService
                .getFilteredAvailableOrders(
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.barbershopId(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.startWeekDate(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.orderFilters()
                );

        verify(orderReservationRepository, times(0)).
                getAvailableOrders(
                        any(), any()
                );
        assertEquals(ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST.size(), unreservedFilteredOrdersForWeek.size());
        assertEquals(ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST, unreservedFilteredOrdersForWeek);
    }

    @Test
    @DisplayName("when order filters (barberIds) were NOT specified returns all available orders")
    void getAvailableFilteredOrdersWithNoFilters() {
        when(orderReservationRepository
                .getAvailableOrders(
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate()
                )
        )
                .thenReturn(ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST);


        List<Order> unreservedFilteredOrdersForWeek = orderReservationService
                .getFilteredAvailableOrders(
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate(),
                        new OrderFilters(List.of())
                );

        verify(orderReservationRepository, times(0)).
                getAvailableFilteredOrders(
                        any(), any(), any()
                );
        assertEquals(ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST.size(), unreservedFilteredOrdersForWeek.size());
        assertEquals(ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST, unreservedFilteredOrdersForWeek);
    }

    @Test
    void reserveOrders() {
        when(orderReservationRepository
                .reserveOrders(
                        ORDER_RESERVATION_VALID_DTO.orderIds(),
                        ORDER_RESERVATION_VALID_DTO.customerId()
                )
        )
                .thenReturn(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST);

        List<Order> reservedFilteredOrdersForWeek = orderReservationService
                .reserveOrders(
                        ORDER_RESERVATION_VALID_DTO.orderIds(),
                        ORDER_RESERVATION_VALID_DTO.customerId()
                );

        assertEquals(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.size(), reservedFilteredOrdersForWeek.size());
        assertEquals(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST, reservedFilteredOrdersForWeek);
    }
}