package com.app.barbershopweb.order.crud.constants;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderDto;

import java.util.List;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.*;

public final class OrderList__TestConstants {

    //note: barbershop should work at ORDER_VALID_ORDER_DATE & the barber should work there
    public static final List<Order> ORDER_VALID_ENTITY_LIST = List.of(
            new Order(
                    ORDER_VALID_ORDER_ID, BARBERSHOP_VALID_BARBERSHOP_ID,
                    1L, ORDER_VALID_CUSTOMER_ID + 1,
                    ORDER_VALID_ORDER_DATE, ORDER_ACTIVE
            ),
            new Order(
                    ORDER_VALID_ORDER_ID + 1, BARBERSHOP_VALID_BARBERSHOP_ID + 1,
                    2L, ORDER_VALID_CUSTOMER_ID,
                    ORDER_VALID_ORDER_DATE.plusDays(1L), ORDER_ACTIVE
            ),
            new Order(
                    ORDER_VALID_ORDER_ID + 2, BARBERSHOP_VALID_BARBERSHOP_ID + 2,
                    3L, ORDER_VALID_CUSTOMER_ID + 1,
                    ORDER_VALID_ORDER_DATE.plusHours(1L), ORDER_ACTIVE
            )
    );


    //note: barbershop should work at ORDER_VALID_ORDER_DATE & the barber should work there
    public static final List<OrderDto> ORDER_VALID_DTO_LIST = List.of(
            new OrderDto(
                    ORDER_VALID_ENTITY_LIST.get(0).getOrderId(),
                    ORDER_VALID_ENTITY_LIST.get(0).getOrderId(),
                    ORDER_VALID_ENTITY_LIST.get(0).getBarberId(),
                    ORDER_VALID_ENTITY_LIST.get(0).getCustomerId(),
                    ORDER_VALID_ENTITY_LIST.get(0).getOrderDate(),
                    ORDER_VALID_ENTITY_LIST.get(0).getActive()
            ),
            new OrderDto(
                    ORDER_VALID_ENTITY_LIST.get(1).getOrderId(),
                    ORDER_VALID_ENTITY_LIST.get(1).getOrderId(),
                    ORDER_VALID_ENTITY_LIST.get(1).getBarberId(),
                    ORDER_VALID_ENTITY_LIST.get(1).getCustomerId(),
                    ORDER_VALID_ENTITY_LIST.get(1).getOrderDate(),
                    ORDER_VALID_ENTITY_LIST.get(1).getActive()
            ),
            new OrderDto(
                    ORDER_VALID_ENTITY_LIST.get(2).getOrderId(),
                    ORDER_VALID_ENTITY_LIST.get(2).getOrderId(),
                    ORDER_VALID_ENTITY_LIST.get(2).getBarberId(),
                    ORDER_VALID_ENTITY_LIST.get(2).getCustomerId(),
                    ORDER_VALID_ENTITY_LIST.get(2).getOrderDate(),
                    ORDER_VALID_ENTITY_LIST.get(2).getActive()
            )
    );

}
