package com.app.barbershopweb.order.crud.constants;

import com.app.barbershopweb.order.crud.Order;

import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.*;

public final class OrderEntity__TestConstants {

    public static final Order ORDER_VALID_ENTITY = new Order(
            ORDER_VALID_ORDER_ID, BARBERSHOP_VALID_BARBERSHOP_ID + 1,
            ORDER_VALID_BARBER_ID, ORDER_VALID_CUSTOMER_ID, ORDER_VALID_ORDER_DATE, ORDER_ACTIVE
    );

    public static final Order ORDER_VALID_UPDATED_ENTITY = new Order(
            ORDER_VALID_ENTITY.getOrderId(), ORDER_VALID_ENTITY.getBarbershopId() + 1,
            ORDER_VALID_ENTITY.getBarberId() + 1, ORDER_VALID_ENTITY.getCustomerId(),
            ORDER_VALID_ENTITY.getOrderDate().plusDays(1L), ORDER_VALID_ENTITY.getActive()
    );

    public static final Order ORDER_INVALID_BUSINESS_ENTITY = new Order(
            ORDER_VALID_ORDER_ID, BARBERSHOP_VALID_BARBERSHOP_ID,
            ORDER_VALID_CUSTOMER_ID, ORDER_VALID_CUSTOMER_ID, ORDER_INVALID_ORDER_DATE, ORDER_ACTIVE
    );

}
