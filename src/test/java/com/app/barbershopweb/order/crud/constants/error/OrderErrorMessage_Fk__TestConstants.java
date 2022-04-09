package com.app.barbershopweb.order.crud.constants.error;

import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.ORDER_VALID_DTO;

public final class OrderErrorMessage_Fk__TestConstants {

    public static final String ORDER_ERR_FK_BARBER_ID = "fk violation: barber with id " +
            ORDER_VALID_DTO.barberId() + " doesn't work at barbershop with id " + ORDER_VALID_DTO.barbershopId();

    public static final String ORDER_ERR_FK_CUSTOMER_ID = "fk violation: customer with id " +
            ORDER_VALID_DTO.customerId() + " not present";

}
