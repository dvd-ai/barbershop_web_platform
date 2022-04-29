package com.app.barbershopweb.order.reservation.constants.list.fk;

import com.app.barbershopweb.barbershop.crud.Barbershop;
import com.app.barbershopweb.user.crud.User;
import com.app.barbershopweb.workspace.Workspace;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public final class OrderReservation_FkEntityList__TestConstants {

    public static final List<User> ORDER_RESERVATION_FK_USER_ENTITY_LIST = List.of(
            new User(1L, "", "",
                    "", "user1@gmail.com", "customer", LocalDateTime.now()),

            new User(2L, "", "",
                    "", "user2@gmail.com", "customer", LocalDateTime.now()),

            new User(3L, "", "",
                    "", "user3@gmail.com", "barber", LocalDateTime.now()),

            new User(4L, "", "",
                    "", "user4@gmail.com", "barber", LocalDateTime.now())
    );

    public static final List<Barbershop> ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST = List.of(
            new Barbershop(1L, "", "", "", "barb1@barbershop.com",
                    LocalTime.of(8, 0), LocalTime.of(20, 0), true),

            new Barbershop(2L, "", "", "", "barb2@barbershop.com",
                    LocalTime.of(9, 0), LocalTime.of(18, 0), true)
    );

    public static final List<Workspace> ORDER_RESERVATION_FK_WORKSPACE_ENTITY_LIST = List.of(
            new Workspace(1L, 3L, 1L, true),
            new Workspace(2L, 3L, 2L, true),
            new Workspace(3L, 4L, 1L, true),
            new Workspace(4L, 4L, 2L, true)
    );

}
