package com.app.barbershopweb.order.reservation;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.reservation.converter.OrderFiltersConverter;
import com.app.barbershopweb.order.reservation.converter.OrderReservationConverter;
import com.app.barbershopweb.order.reservation.converter.ShowUnreservedOrdersConverter;
import com.app.barbershopweb.order.reservation.dto.OrderFiltersDto;
import com.app.barbershopweb.order.reservation.dto.OrderReservationDto;
import com.app.barbershopweb.order.reservation.dto.ShowUnreservedOrdersDto;
import com.app.barbershopweb.order.reservation.entity.OrderFilters;
import com.app.barbershopweb.order.reservation.entity.OrderReservation;
import com.app.barbershopweb.order.reservation.entity.ShowUnreservedOrders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("orders/reservations")
@Validated
public class OrderReservationController {

    private final OrderReservationService orderReservationService;
    private final OrderConverter orderConverter;
    private final ShowUnreservedOrdersConverter showUnreservedOrdersConverter;
    private final OrderFiltersConverter orderFiltersConverter;
    private final OrderReservationConverter orderReservationConverter;

    public OrderReservationController(OrderReservationService orderReservationService, OrderConverter orderConverter, ShowUnreservedOrdersConverter showUnreservedOrdersConverter, OrderFiltersConverter orderFiltersConverter, OrderReservationConverter orderReservationConverter) {
        this.orderReservationService = orderReservationService;
        this.orderConverter = orderConverter;
        this.showUnreservedOrdersConverter = showUnreservedOrdersConverter;
        this.orderFiltersConverter = orderFiltersConverter;
        this.orderReservationConverter = orderReservationConverter;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>>getBarbershopActiveUnreservedOrdersForWeek(
            @RequestBody @Valid ShowUnreservedOrdersDto showUnreservedOrdersDto
    ) {
        ShowUnreservedOrders entity = showUnreservedOrdersConverter.mapToEntity(showUnreservedOrdersDto);
        List<Order> orders = orderReservationService.getBarbershopActiveUnreservedOrdersForWeek(
                entity.getBarbershopId(),
                entity.getReservationDateToStartWeekFrom()
        );
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(orders), HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getBarbershopActiveFilteredUnreservedOrdersForWeek(
            @RequestBody @Valid ShowUnreservedOrdersDto showUnreservedOrdersDto,
            @RequestBody @Valid OrderFiltersDto orderFiltersDto
            ) {
        ShowUnreservedOrders entity = showUnreservedOrdersConverter.mapToEntity(showUnreservedOrdersDto);
        OrderFilters filters = orderFiltersConverter.mapToEntity(orderFiltersDto);

        List<Order> orders = orderReservationService.getBarbershopFilteredActiveUnreservedOrdersForWeek(
                entity.getBarbershopId(),
                entity.getReservationDateToStartWeekFrom(),
                filters
        );
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(orders), HttpStatus.OK
        );
    }

    @PutMapping()
    public ResponseEntity<List<OrderDto>> reserveCustomerOrders(OrderReservationDto orderReservationDto) {
        OrderReservation entity = orderReservationConverter.mapToEntity(orderReservationDto);
        List<Order> reservedOrders =
                orderReservationService.reserveCustomerOrders(
                        entity.getOrderIds(),
                        entity.getCustomerId()
                );
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(reservedOrders),
                HttpStatus.OK
        );
    }

}