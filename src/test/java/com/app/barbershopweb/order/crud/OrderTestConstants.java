package com.app.barbershopweb.order.crud;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public final class OrderTestConstants {
    public final int ORDER_FIELD_AMOUNT = 6;
    public final static String ORDERS_URL = "/orders";

    public final long INVALID_ORDER_ID = -100L;
    public final long VALID_ORDER_ID = 1L;
    public final long VALID_BARBERSHOP_ID = 1L;
    public final long VALID_CUSTOMER_ID = 1L;
    public final long VALID_BARBER_ID = 2L;
    public final LocalDateTime VALID_ORDER_DATE = LocalDateTime.of(
            2022, 3, 30, 12, 0
    );

    public final long NOT_EXISTED_ORDER_ID = 100_000L;

    public final boolean ACTIVE = true;

    public final LocalTime TIME_FROM = LocalTime.of(8, 0, 0);
    public final LocalTime TIME_TO = LocalTime.of(20, 0, 0);

    public final Order VALID_ORDER_ENTITY = new Order(
            VALID_ORDER_ID, VALID_BARBERSHOP_ID,
            VALID_BARBER_ID, VALID_CUSTOMER_ID, VALID_ORDER_DATE, ACTIVE
    );

//    public final Order VALID_UPDATED_ORDER_ENTITY = new Order(
//            VALID_ORDER_ID, 2L,
//            1L, ACTIVE
//    );

//    public final Order ORDER_ENTITY_NOT_EXISTED_ID = new Order(
//            NOT_EXISTED_ORDER_ID, VALID_USER_ID,
//            VALID_BARBERSHOP_ID, ACTIVE
//    );

//    public final OrderDto VALID_ORDER_DTO = new OrderDto(
//            VALID_ORDER_ID, VALID_USER_ID,
//            VALID_BARBERSHOP_ID, ACTIVE
//    );

//    public final OrderDto INVALID_ORDER_DTO = new OrderDto(
//            INVALID_ORDER_ID, -100L,
//            0L, ACTIVE
//    );

//    public final OrderDto ORDER_DTO_NOT_EXISTED_ID = new OrderDto(
//            NOT_EXISTED_ORDER_ID, VALID_USER_ID,
//            VALID_BARBERSHOP_ID, ACTIVE
//    );

//    public final OrderDto VALID_UPDATED_ORDER_DTO = new OrderDto(
//            VALID_ORDER_ID, 1L,
//            2L, ACTIVE
//    );

    public final List<Long>barberIds = List.of(2L, 4L);
    public final List<Long>customerIds = List.of(1L, 3L);

    //note: barbershop should work at VALID_ORDER_DATE & the barber can work there
    public final List<Order> VALID_ORDER_ENTITY_LIST = List.of(
            new Order(
                    VALID_ORDER_ID, VALID_BARBERSHOP_ID,
                    barberIds.get(0), customerIds.get(0),
                    VALID_ORDER_DATE, ACTIVE
            ),
            new Order(
                    VALID_ORDER_ID + 1, VALID_BARBERSHOP_ID,
                    barberIds.get(0), customerIds.get(0),
                    VALID_ORDER_DATE.plusDays(1L), ACTIVE
            ),
            new Order(
                    VALID_ORDER_ID + 2, VALID_BARBERSHOP_ID + 1,
                    barberIds.get(1), customerIds.get(1),
                    VALID_ORDER_DATE, ACTIVE
            )
    );


    //note: barbershop should work at VALID_ORDER_DATE & the barber can work there
    public final List<OrderDto> VALID_ORDER_DTO_LIST = List.of(
            new OrderDto(
                    VALID_ORDER_ID, VALID_BARBERSHOP_ID,
                    barberIds.get(0), customerIds.get(0),
                    VALID_ORDER_DATE, ACTIVE
            ),
            new OrderDto(
                    VALID_ORDER_ID + 1, VALID_BARBERSHOP_ID,
                    barberIds.get(0), customerIds.get(0),
                    VALID_ORDER_DATE.plusDays(1L), ACTIVE
            ),
            new OrderDto(
                    VALID_ORDER_ID + 2, VALID_BARBERSHOP_ID + 1,
                    barberIds.get(1), customerIds.get(1),
                    VALID_ORDER_DATE, ACTIVE
            )
    );

    //ERROR MESSAGES:

    //CV means 'constraint violation'
    public final String DTO_CV_ORDER_ID_ERR_MSG = "'workspaceDto.workspaceId' must be greater than or equal to 1";
    public final String DTO_CV_USER_ID_ERR_MSG = "'workspaceDto.userId' must be greater than or equal to 1";
    public final String DTO_CV_BARBERSHOP_ID_ERR_MSG = "'workspaceDto.barbershopId' must be greater than or equal to 1";

    //PV means 'path variable'
    public final String PV_ORDER_ID_ERR_MSG = "'workspaceId' must be greater than or equal to 1";

//    //fk, uk violation
//    public final String FK_CV_USER_ID_ERR_MSG = "fk violation: user with id " +
//            VALID_ORDER_DTO.userId()  + " not present";
//
//    public final String FK_CV_BARBERSHOP_ID_ERR_MSG = "fk violation: barbershop with id " +
//            VALID_ORDER_DTO.barbershopId()  + " not present";
//
//    public final String UK_CV_ERR_MSG = "uk violation: workspace with user id " + VALID_ORDER_DTO.userId() +
//            " and barbershop id " + VALID_ORDER_DTO.barbershopId() + " already exists.";

}
