package com.app.barbershopweb.order.reservation.constants.error;

public final class OrderReservation_ErrorMessage_Dto__TestConstants {

    public static final String GET_OPEN_ORDERS_ERR_INVALID_DTO_BARBERSHOP_ID =
            "'getOpenOrdersRequestDto.barbershopId' must be greater than or equal to 1";

    public static final String GET_OPEN_ORDERS_ERR_INVALID_DTO_START_WEEK_DATE =
            "'getOpenOrdersRequestDto.startWeekDate' must not be null";

    public static final String ORDER_RESERVATION_ERR_INVALID_DTO_CUSTOMER_ID =
            "'orderReservationDto.customerId' must be greater than or equal to 1";

    public static final String ORDER_RESERVATION_ERR_INVALID_DTO_ORDER_ID_LIST =
            "'orderReservationDto.orderIds' must not be empty";


    public static final String GET_OPEN_FILTERED_ORDERS_ERR_INVALID_DTO_ORDER_FILTERS =
            "'getOpenFilteredOrdersRequestDto.orderFilters' must not be null";

    public static final String GET_OPEN_FILTERED_ORDERS_ERR_INVALID_DTO_BARBERSHOP_ID =
            "'getOpenFilteredOrdersRequestDto.barbershopId' must be greater than or equal to 1";

    public static final String GET_OPEN_FILTERED_ORDERS_ERR_INVALID_DTO_START_WEEK_DATE =
            "'getOpenFilteredOrdersRequestDto.startWeekDate' must not be null";

}
