package com.app.barbershopweb.order.reservation.constants.list.fk;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.workspace.Workspace;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public final class OrderReservation_FkEntityList__TestConstants {

    public static final List<Users> ORDER_RESERVATION_FK_USER_ENTITY_LIST = List.of(
            new Users(1L, "", "",
                    "", "", "customer", LocalDateTime.now()),

            new Users(2L, "", "",
                    "", "", "customer", LocalDateTime.now()),

            new Users(3L, "", "",
                    "", "", "barber", LocalDateTime.now()),

            new Users(4L, "", "",
                    "", "", "barber", LocalDateTime.now())
    );

    public static final List<Barbershop> ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST = List.of(
            new Barbershop(1L, "", "", "", "",
                    LocalTime.of(8, 0), LocalTime.of(20, 0)),

            new Barbershop(2L, "", "", "", "",
                    LocalTime.of(9, 0), LocalTime.of(18, 0))
    );

    public static final List<Workspace> ORDER_RESERVATION_FK_WORKSPACE_ENTITY_LIST = List.of(
            new Workspace(1L, 3L, 1L, true),
            new Workspace(2L, 3L, 2L, true),
            new Workspace(3L, 4L, 1L, true),
            new Workspace(4L, 4L, 2L, true)
    );

}
