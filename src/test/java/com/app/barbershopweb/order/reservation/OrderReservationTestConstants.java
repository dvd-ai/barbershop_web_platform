package com.app.barbershopweb.order.reservation;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.reservation.dto.OrderReservationDto;
import com.app.barbershopweb.order.reservation.dto.ShowUnreservedOrdersRequestDto;
import com.app.barbershopweb.order.reservation.entity.OrderFilters;
import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.workspace.Workspace;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

public final class OrderReservationTestConstants {

    public static final String ORDER_RESERV_URL = "/orders/reservations";
    public static final String ORDER_RESERV_FILTER_URL = ORDER_RESERV_URL + "/filtered";

    public final long BARBER_FILTER = 3L;

    //SUOR - show unreserved orders request
    public final LocalDateTime SUOR_START_WEEK_DATE = LocalDateTime.of(2022, 3,
            28, 12, 30
    );


    public final ShowUnreservedOrdersRequestDto SUOR_DTO_NO_FILTERS = new ShowUnreservedOrdersRequestDto(
      1L, SUOR_START_WEEK_DATE, new OrderFilters(List.of())
    );

    public final ShowUnreservedOrdersRequestDto SUOR_DTO_WITH_FILTERS = new ShowUnreservedOrdersRequestDto(
            1L, SUOR_START_WEEK_DATE, new OrderFilters(List.of(BARBER_FILTER))
    );

    public final ShowUnreservedOrdersRequestDto INVALID_SUOR_DTO_NO_FILTERS = new ShowUnreservedOrdersRequestDto(
            0L, null, null
    );

    public final OrderReservationDto INVALID_ORDER_RESERV_DTO = new OrderReservationDto(
            List.of(), 0L
    );

    public final OrderReservationDto VALID_ORDER_RESERV_DTO = new OrderReservationDto(
            List.of(1L, 2L, 3L), 1L
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

        new Order(3L, FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                FK_USER_ENTITY_LIST.get(2).getId(), null,
                LocalDateTime.of(2022, 4, 3, 14, 0),
                true
        ),

        new Order(4L, FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                FK_USER_ENTITY_LIST.get(3).getId(), null,
                LocalDateTime.of(2022, 4, 2, 14, 0),
                true
        )

    );

    public final List<Order> NOT_SUITABLE_UNRESERVED_ORDER_ENTITY_LIST = List.of (
            new Order(1L, FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                    FK_USER_ENTITY_LIST.get(2).getId(), null,
                    LocalDateTime.of(2022, 4, 2, 14, 0),
                    false
            ),
            new Order(2L, FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                    FK_USER_ENTITY_LIST.get(2).getId(), null,
                    LocalDateTime.of(2021, 4, 2, 15, 0),
                    true
            ),

            new Order(3L, FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                    FK_USER_ENTITY_LIST.get(2).getId(), 1L,
                    LocalDateTime.of(2022, 4, 3, 14, 0),
                    true
            ),

            new Order(4L, FK_BARBERSHOP_ENTITY_LIST.get(0).getId(),
                    FK_USER_ENTITY_LIST.get(3).getId(), 2L,
                    LocalDateTime.of(2022, 11, 2, 10, 0),
                    false
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

    public final List<Order> UNRESERVED_FILTERED_ORDER_ENTITY_LIST = UNRESERVED_ORDER_ENTITY_LIST
            .stream()
            .filter(i -> i.getBarberId().equals(BARBER_FILTER))
            .toList();


    private final Function<Order, OrderDto> mapToDto = item -> new OrderDto(
            item.getOrderId(),
            item.getBarbershopId(),
            item.getBarberId(),
            item.getCustomerId(),
            item.getOrderDate(),
            item.getActive()
    );

    public final List<OrderDto> UNRESERVED_FILTERED_ORDER_DTO_LIST = UNRESERVED_FILTERED_ORDER_ENTITY_LIST
            .stream()
            .map(mapToDto)
            .toList();


    public final List<Order> RESERVED_ORDER_ENTITY_LIST = List.of(
            new Order(
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getOrderId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getBarbershopId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getBarberId(),
                    VALID_ORDER_RESERV_DTO.customerId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getOrderDate(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(0).getActive()
            ),
            new Order(
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getOrderId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getBarbershopId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getBarberId(),
                    VALID_ORDER_RESERV_DTO.customerId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getOrderDate(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(1).getActive()
            ),
            new Order(
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getOrderId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getBarbershopId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getBarberId(),
                    VALID_ORDER_RESERV_DTO.customerId(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getOrderDate(),
                    UNRESERVED_ORDER_ENTITY_LIST.get(2).getActive()
            )
    );

    public final List<OrderDto> RESERVED_ORDER_DTO_LIST = RESERVED_ORDER_ENTITY_LIST
            .stream()
            .map(mapToDto)
            .toList()
    ;



    //CV means 'constraint violation'
    public final String DTO_CV_ORDER_FILTERS_ERR_MSG = "'showUnreservedOrdersRequestDto.orderFilters' must not be null";
    public final String DTO_CV_BARBERSHOP_ID_ERR_MSG = "'showUnreservedOrdersRequestDto.barbershopId' must be greater than or equal to 1";
    public final String DTO_CV_START_WEEK_DATE_ERR_MSG = "'showUnreservedOrdersRequestDto.startWeekDate' must not be null";
    public final String DTO_CV_CUSTOMER_ID_ERR_MSG = "'orderReservationDto.customerId' must be greater than or equal to 1";
    public final String DTO_CV_ORDER_ID_LIST_ERR_MSG = "'orderReservationDto.orderIds' must not be empty";

    public final String FK_CV_CUSTOMER_ID_ERR_MSG = "Customer with id " +
            VALID_ORDER_RESERV_DTO.customerId() +
            " wasn't found during order reservation";

    public final List<String> FK_CV_ORDER_ID_LIST_ERR_MSG = VALID_ORDER_RESERV_DTO.orderIds()
            .stream()
            .map(id -> "Order with id " + id +  " wasn't found during order reservation")
            .toList();

    public final String UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG =
            "uk violation during order reservation (" +
                    "orderId " +  RESERVED_ORDER_ENTITY_LIST.get(0).getOrderId() + ") :" +
                    " order with" +
                    " customerId " + RESERVED_ORDER_ENTITY_LIST.get(0).getCustomerId() +
                    " and orderDate " + RESERVED_ORDER_ENTITY_LIST.get(0).getOrderDate() +
                    " already exists"
    ;




}
