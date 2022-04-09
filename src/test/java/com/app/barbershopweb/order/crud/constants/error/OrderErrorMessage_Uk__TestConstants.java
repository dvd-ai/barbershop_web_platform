package com.app.barbershopweb.order.crud.constants.error;

import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.ORDER_VALID_DTO;

public final class OrderErrorMessage_Uk__TestConstants {

    public static final String ORDER_ERR_UK_BARBER_ID__ORDER_DATE = "uk violation: order with barberId " + ORDER_VALID_DTO.barberId() +
            " and orderDate " + ORDER_VALID_DTO.orderDate() + " already exists";

    public static final String ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE =
            "uk violation: order with customerId "
                    + ORDER_VALID_DTO.customerId() +
                    " and orderDate " + ORDER_VALID_DTO.orderDate() + " already exists";

}
