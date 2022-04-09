package com.app.barbershopweb.order.reservation.constants.list.dto;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderDto;

import java.util.List;
import java.util.function.Function;

import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.*;

public final class OrderReservation_List_OrderDto__TestConstants {

    private static final Function<Order, OrderDto> mapToDto = item ->
            new OrderConverter().mapToDto(item);


    public static final List<OrderDto> ORDER_RESERVATION_OPEN_ORDER_DTO_LIST =
            ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST
                    .stream()
                    .map(mapToDto)
                    .toList();

    public static final List<OrderDto> ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST =
            ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST
                    .stream()
                    .map(mapToDto)
                    .toList();

    public static final List<OrderDto> ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST =
            ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST
                    .stream()
                    .map(mapToDto)
                    .toList();
}
