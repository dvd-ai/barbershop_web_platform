package com.app.barbershopweb.order.reservation.constants.list.entity;

import com.app.barbershopweb.order.crud.Order;

import java.time.LocalDateTime;
import java.util.List;

import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.GET_OPEN_ORDERS_BARBER_FILTER;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.ORDER_RESERVATION_FK_USER_ENTITY_LIST;

public final class OrderReservation_List_OrderEntity__TestConstants {

    public static final List<Order> ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST = List.of(
            new Order(1L, ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                    ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(2).getId(), null,
                    LocalDateTime.of(2022, 4, 2, 14, 0),
                    true
            ),
            new Order(2L, ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                    ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(2).getId(), null,
                    LocalDateTime.of(2022, 4, 2, 15, 0),
                    true
            ),

            new Order(3L, ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                    ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(2).getId(), null,
                    LocalDateTime.of(2022, 4, 3, 14, 0),
                    true
            ),

            new Order(4L, ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                    ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(3).getId(), null,
                    LocalDateTime.of(2022, 4, 2, 14, 0),
                    true
            )

    );

    public static final List<Order> ORDER_RESERVATION_UNFIT_OPEN_ORDER_ENTITY_LIST = List.of(
            new Order(4L, ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                    ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(3).getId(), 2L,
                    LocalDateTime.of(2022, 11, 2, 10, 0),
                    false
            )
    );

    public static final List<Order> ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST =
            ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST
                    .stream()
                    .filter(i -> i.getBarberId().equals(GET_OPEN_ORDERS_BARBER_FILTER))
                    .toList();

    public static final List<Order> ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST =
            ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST
                    .stream()
                    .limit(3L)
                    .map(order -> new Order(
                                    order.getOrderId(),
                                    order.getBarbershopId(),
                                    order.getBarberId(),
                                    ORDER_RESERVATION_VALID_DTO.customerId(),
                                    order.getOrderDate(),
                                    order.getActive()
                            )
                    )
                    .toList();
}
