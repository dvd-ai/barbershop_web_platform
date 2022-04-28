package com.app.barbershopweb.barbershop.closure.constants;

import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST;

public final class BarbershopClosure_ErrorMessage__TestConstants {
    public final static String BARBERSHOP_CLOSURE_ERR_BARBERSHOP_NOT_FOUND =
            "Barbershop closure (out of business): No barbershop with id "
                    + ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.get(0).getId() +
                    " found.";
}
