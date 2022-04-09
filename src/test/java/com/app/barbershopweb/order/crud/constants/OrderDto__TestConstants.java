package com.app.barbershopweb.order.crud.constants;

import com.app.barbershopweb.order.crud.OrderDto;

import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_INVALID_BUSINESS_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_ACTIVE;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_VALID_ORDER_DATE;

public final class OrderDto__TestConstants {

    public static final OrderDto ORDER_VALID_DTO = new OrderDto(
            ORDER_VALID_ENTITY.getOrderId(), ORDER_VALID_ENTITY.getBarbershopId(),
            ORDER_VALID_ENTITY.getBarberId(), ORDER_VALID_ENTITY.getCustomerId(),
            ORDER_VALID_ENTITY.getOrderDate(), ORDER_VALID_ENTITY.getActive()
    );

    public static final OrderDto ORDER_VALID_UPDATED_DTO = new OrderDto(
            ORDER_VALID_ENTITY.getOrderId(), ORDER_VALID_ENTITY.getBarbershopId() + 1,
            ORDER_VALID_ENTITY.getBarberId() + 1, ORDER_VALID_ENTITY.getCustomerId(),
            ORDER_VALID_ENTITY.getOrderDate().plusDays(1L), ORDER_VALID_ENTITY.getActive()
    );

    public static final OrderDto ORDER_INVALID_DTO = new OrderDto(
            0L, 0L,
            0L, 0L, ORDER_VALID_ORDER_DATE, ORDER_ACTIVE
    );

    public static final OrderDto ORDER_INVALID_BUSINESS_DTO = new OrderDto(
            ORDER_INVALID_BUSINESS_ENTITY.getOrderId(),
            ORDER_INVALID_BUSINESS_ENTITY.getBarbershopId(),
            ORDER_INVALID_BUSINESS_ENTITY.getBarberId(),
            ORDER_INVALID_BUSINESS_ENTITY.getCustomerId(),
            ORDER_INVALID_BUSINESS_ENTITY.getOrderDate(),
            ORDER_INVALID_BUSINESS_ENTITY.getActive()
    );

}
