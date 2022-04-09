package com.app.barbershopweb.order.reservation.constants.error;

import java.util.List;

import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_VALID_DTO;

public final class OrderReservation_ErrorMessage_Fk__TestConstants {

    public static final String ORDER_RESERVATION_ERR_FK_CUSTOMER_ID = "Customer with id " +
            ORDER_RESERVATION_VALID_DTO.customerId() +
            " wasn't found during order reservation";

    public static final List<String> ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST =
            ORDER_RESERVATION_VALID_DTO.orderIds()
                    .stream()
                    .map(id -> "Order with id " + id + " wasn't found during order reservation")
                    .toList();

}
