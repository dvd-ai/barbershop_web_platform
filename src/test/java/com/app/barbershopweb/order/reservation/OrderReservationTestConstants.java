package com.app.barbershopweb.order.reservation;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.reservation.dto.ShowUnreservedOrdersRequestDto;
import com.app.barbershopweb.order.reservation.entity.OrderFilters;
import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.workspace.Workspace;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public final class OrderReservationTestConstants {

    public static final String ORDER_RESERV_URL = "/orders/reservations";
    public static final String ORDER_RESERV_FILTER_URL = ORDER_RESERV_URL + "/filtered";

    public final LocalDateTime SUOR_START_WEEK_DATE = LocalDateTime.of(2022, 3,
            28, 12, 30
    );


    public final ShowUnreservedOrdersRequestDto SUOR_DTO_NO_FILTERS = new ShowUnreservedOrdersRequestDto(
      1L, SUOR_START_WEEK_DATE, new OrderFilters()
    );

    public final ShowUnreservedOrdersRequestDto INVALID_SUOR_DTO_NO_FILTERS = new ShowUnreservedOrdersRequestDto(
            0L, null, null
    );

    public final List<Users> FK_USER_ENTITY_LIST = List.of(
        new Users(1L, "", "",
                "", "", "customer", LocalDateTime.now()),

        new Users(2L, "", "",
                "", "", "customer", LocalDateTime.now()),

        new Users(3L, "", "",
                "", "", "barber", LocalDateTime.now()),

        new Users(4L, "", "",
            "", "", "barber", LocalDateTime.now())
    );

    public final List<Barbershop> FK_BARBERSHOP_ENTITY_LIST = List.of(
        new Barbershop(1L, "", "", "", "",
                LocalTime.of(8, 0), LocalTime.of(20, 0)),

        new Barbershop(2L, "", "", "", "",
                LocalTime.of(9, 0), LocalTime.of(18, 0))
    );

    public final List<Workspace> FK_WORKSPACE_ENTITY_LIST = List.of(
        new Workspace(1L, 3L, 1L, true),
        new Workspace(2L, 3L, 2L, true),
        new Workspace(3L, 4L, 1L, true),
        new Workspace(4L, 4L, 2L, true)
    );

    public final List<Order> UNRESERVED_ORDER_ENTITY_LIST = List.of(
        new Order(1L, FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                FK_USER_ENTITY_LIST.get(2).getId(), null,
                LocalDateTime.of(2022, 4, 2, 14, 0),
                true
        ),
        new Order(2L, FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                FK_USER_ENTITY_LIST.get(2).getId(), null,
                LocalDateTime.of(2022, 4, 2, 15, 0),
                true
        ),
        new Order(3L, FK_BARBERSHOP_ENTITY_LIST.get(1).getId(),
                FK_USER_ENTITY_LIST.get(3).getId(), null,
                LocalDateTime.of(2022, 4, 2, 14, 0),
                true
        ),
        new Order(4L, FK_BARBERSHOP_ENTITY_LIST.get(1).getId(),
                FK_USER_ENTITY_LIST.get(2).getId(), null,
                LocalDateTime.of(2022, 4, 3, 14, 0),
                true
        )
    );

    public final List<OrderDto> UNRESERVED_ORDER_DTO_LIST = List.of(
            new OrderDto(
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getOrderId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getBarbershopId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getBarberId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getCustomerId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getOrderDate(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getActive()
            ),
            new OrderDto(
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getOrderId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getBarbershopId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getBarberId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getCustomerId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getOrderDate(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getActive()
            ),
            new OrderDto(
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getOrderId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getBarbershopId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getBarberId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getCustomerId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getOrderDate(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getActive()
            ),
            new OrderDto(
                    UNRESERVED_ORDER_ENTITY_LIST.get(3).getOrderId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(3).getBarbershopId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(3).getBarberId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(3).getCustomerId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(3).getOrderDate(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(3).getActive()
            )
    );


    //CV means 'constraint violation'
    public final String DTO_CV_ORDER_FILTERS_ERR_MSG = "'showUnreservedOrdersRequestDto.orderFilters' must not be null";
    public final String DTO_CV_BARBERSHOP_ID_ERR_MSG = "'showUnreservedOrdersRequestDto.barbershopId' must be greater than or equal to 1";
    public final String DTO_CV_START_WEEK_DATE_ERR_MSG = "'showUnreservedOrdersRequestDto.startWeekDate' must not be null";

}
