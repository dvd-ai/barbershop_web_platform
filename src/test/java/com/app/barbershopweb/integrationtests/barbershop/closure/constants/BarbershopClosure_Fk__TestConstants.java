package com.app.barbershopweb.integrationtests.barbershop.closure.constants;

import com.app.barbershopweb.order.crud.Order;

import java.time.LocalDateTime;

public final class BarbershopClosure_Fk__TestConstants {

    public static final Order BARBERSHOP_CLOSURE_FK_ORDER = new Order(
            1L, 1L, 3L,
            1L,
            LocalDateTime.of(2022, 4, 30, 14, 0),
            true
    );

}
