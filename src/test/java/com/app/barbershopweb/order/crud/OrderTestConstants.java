package com.app.barbershopweb.order.crud;

import com.app.barbershopweb.barbershop.BarbershopTestConstants;

import java.time.LocalDateTime;
import java.util.List;

public final class OrderTestConstants {
    public final BarbershopTestConstants btc = new BarbershopTestConstants();

    public final int ORDER_FIELD_AMOUNT = 6;
    public final static String ORDERS_URL = "/orders";

    public final long INVALID_ORDER_ID = -100L;
    public final long VALID_ORDER_ID = 1L;
    public final long NOT_EXISTING_ORDER_ID = 100_000L;
    public final long VALID_CUSTOMER_ID = 1L;
    public final long VALID_BARBER_ID = 2L;
    public final LocalDateTime VALID_ORDER_DATE = LocalDateTime.of(
            2022, 3, 30,
            btc.VALID_BARBERSHOP_ENTITY.getWorkTimeFrom()
                    .getHour(),
            0
    );
    //hours shouldn't match to barbershop order hours,
    // minutes shouldn't be like '00';
    public final LocalDateTime INVALID_ORDER_DATE =
            VALID_ORDER_DATE
                    .minusHours(5L)
                    .minusMinutes(30L);

    public final boolean ACTIVE = true;

    public final Order VALID_ORDER_ENTITY = new Order(
            VALID_ORDER_ID, btc.VALID_BARBERSHOP_ID + 1,
            VALID_BARBER_ID, VALID_CUSTOMER_ID, VALID_ORDER_DATE, ACTIVE
    );

    public final Order VALID_UPDATED_ORDER_ENTITY = new Order(
            VALID_ORDER_ID, btc.VALID_BARBERSHOP_ID + 2,
            VALID_BARBER_ID + 1, VALID_CUSTOMER_ID, VALID_ORDER_DATE.plusDays(1L), ACTIVE
    );

//    public final Order ORDER_ENTITY_NOT_EXISTED_ID = new Order(
//            NOT_EXISTING_ORDER_ID, VALID_USER_ID,
//            VALID_BARBERSHOP_ID, ACTIVE
//    );

    public final OrderDto VALID_ORDER_DTO = new OrderDto(
            VALID_ORDER_ID, btc.VALID_BARBERSHOP_ID,
            VALID_BARBER_ID, VALID_CUSTOMER_ID, VALID_ORDER_DATE, ACTIVE
    );

    public final OrderDto VALID_UPDATED_ORDER_DTO = new OrderDto(
            VALID_ORDER_ID, btc.VALID_BARBERSHOP_ID + 1,
            VALID_BARBER_ID, VALID_CUSTOMER_ID, VALID_ORDER_DATE, ACTIVE
    );

    public final OrderDto INVALID_ORDER_DTO = new OrderDto(
            0L, 0L,
            0L, 0L, VALID_ORDER_DATE, ACTIVE
    );

    public final OrderDto INVALID_BDF_ORDER_DTO = new OrderDto(
            VALID_ORDER_ID, btc.VALID_BARBERSHOP_ID,
            VALID_CUSTOMER_ID, VALID_CUSTOMER_ID, INVALID_ORDER_DATE, ACTIVE
    );

//    public final OrderDto ORDER_DTO_NOT_EXISTED_ID = new OrderDto(
//            NOT_EXISTING_ORDER_ID, VALID_USER_ID,
//            VALID_BARBERSHOP_ID, ACTIVE
//    );

    public final List<Long>barberIds = List.of(2L, 4L);
    public final List<Long>customerIds = List.of(1L, 3L);

    //note: barbershop should work at VALID_ORDER_DATE & the barber can work there
    public final List<Order> VALID_ORDER_ENTITY_LIST = List.of(
            new Order(
                    VALID_ORDER_ID, btc.VALID_BARBERSHOP_ID,
                    1L, VALID_CUSTOMER_ID + 1,
                    VALID_ORDER_DATE, ACTIVE
            ),
            new Order(
                    VALID_ORDER_ID + 1, btc.VALID_BARBERSHOP_ID + 1,
                    2L, VALID_CUSTOMER_ID,
                    VALID_ORDER_DATE.plusDays(1L), ACTIVE
            ),
            new Order(
                    VALID_ORDER_ID + 2, btc.VALID_BARBERSHOP_ID + 2,
                    3L, VALID_CUSTOMER_ID + 1,
                    VALID_ORDER_DATE.plusHours(1L), ACTIVE
            )
    );


    //note: barbershop should work at VALID_ORDER_DATE & the barber can work there
    public final List<OrderDto> VALID_ORDER_DTO_LIST = List.of(
            new OrderDto(
                    VALID_ORDER_ID, btc.VALID_BARBERSHOP_ID,
                    barberIds.get(0), customerIds.get(0),
                    VALID_ORDER_DATE, ACTIVE
            ),
            new OrderDto(
                    VALID_ORDER_ID + 1, btc.VALID_BARBERSHOP_ID,
                    barberIds.get(0), customerIds.get(0),
                    VALID_ORDER_DATE.plusDays(1L), ACTIVE
            ),
            new OrderDto(
                    VALID_ORDER_ID + 2, btc.VALID_BARBERSHOP_ID + 1,
                    barberIds.get(1), customerIds.get(1),
                    VALID_ORDER_DATE, ACTIVE
            )
    );

    //ERROR MESSAGES:

    //CV means 'constraint violation'
    public final String DTO_CV_ORDER_ID_ERR_MSG = "'orderDto.orderId' must be greater than or equal to 1";
    public final String DTO_CV_CUSTOMER_ID_ERR_MSG = "'orderDto.customerId' must be greater than or equal to 1";
    public final String DTO_CV_BARBER_ID_ERR_MSG = "'orderDto.barberId' must be greater than or equal to 1";
    public final String DTO_CV_BARBERSHOP_ID_ERR_MSG = "'orderDto.barbershopId' must be greater than or equal to 1";


    //PV means 'path variable'
    public final String PV_ORDER_ID_ERR_MSG = "'orderId' must be greater than or equal to 1";

    //fk, uk violation
    public final String FK_CV_BARBER_ID_ERR_MSG = "fk violation: barber with id " +
            VALID_ORDER_DTO.barberId()  + " doesn't work at barbershop with id " + VALID_ORDER_DTO.barbershopId();

    public final String FK_CV_CUSTOMER_ID_ERR_MSG = "fk violation: customer with id " +
            VALID_ORDER_DTO.customerId()  + " not present";

    public final String UK_CV_BARBER_ID_ORDER_DATE_ERR_MSG = "uk violation: order with barberId " + VALID_ORDER_DTO.barberId() +
            " and orderDate " + VALID_ORDER_DTO.orderDate() + " already exists.";

    public final String UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG =
            "uk violation: order with customerId "
            + VALID_ORDER_DTO.customerId() +
            " and orderDate " + VALID_ORDER_DTO.orderDate() + " already exists.";

    //BDF - means business data format
    public final String BDF_CV_CUSTOMER_ID_BARBER_ID_EQ_ERR_MSG =
            "Order with id " + INVALID_BDF_ORDER_DTO.orderId() +
                    " shouldn't have equal customerId and barberId";

    public final String BDF_CV_BARBERSHOP_HOURS_ERR_MSG =
            "orderDate with time " + INVALID_BDF_ORDER_DTO.orderDate().
                    toLocalTime() + " violates barbershop order hours (" +
                    btc.VALID_BARBERSHOP_ENTITY.getWorkTimeFrom() + " - " +
                    btc.VALID_BARBERSHOP_ENTITY.getWorkTimeTo().minusHours(1L) +
                    ")";

    public final String BDF_CV_TIME_FORMAT_ERR_MSG =
            "orderDate with time " + INVALID_BDF_ORDER_DTO.orderDate().
                    toLocalTime() + " should hourly formatted";


}
