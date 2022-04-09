package com.app.barbershopweb.order.reservation.constants.error;

import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST;

public final class OrderReservation_ErrorMessage_Uk__TestConstants {

    public static final String ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE =
            "uk violation during order reservation (" +
                    "orderId " + ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.get(0).getOrderId() + ") :" +
                    " order with" +
                    " customerId " + ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.get(0).getCustomerId() +
                    " and orderDate " + ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.get(0).getOrderDate() +
                    " already exists";
}
