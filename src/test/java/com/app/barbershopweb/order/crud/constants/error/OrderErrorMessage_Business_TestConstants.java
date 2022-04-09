package com.app.barbershopweb.order.crud.constants.error;

import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.ORDER_INVALID_BUSINESS_DTO;

public final class OrderErrorMessage_Business_TestConstants {

    public static final String ORDER_ERR_BUSINESS_CUSTOMER_ID_BARBER_ID =
            "Order with id " + ORDER_INVALID_BUSINESS_DTO.orderId() +
                    " shouldn't have equal customerId and barberId";

    public static final String ORDER_ERR_BUSINESS_BARBERSHOP_HOURS =
            "orderDate with time " + ORDER_INVALID_BUSINESS_DTO.orderDate().
                    toLocalTime() + " violates barbershop order hours (" +
                    BARBERSHOP_VALID_ENTITY.getWorkTimeFrom() + " - " +
                    BARBERSHOP_VALID_ENTITY.getWorkTimeTo().minusHours(1L) +
                    ")";

    public static final String ORDER_ERR_BUSINESS_TIME_FORMAT =
            "orderDate with time " + ORDER_INVALID_BUSINESS_DTO.orderDate().
                    toLocalTime() + " should be hourly formatted";


}
