package com.app.barbershopweb.order.reservation;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.reservation.dto.OrderReservationDto;
import com.app.barbershopweb.order.reservation.dto.ShowUnreservedOrdersRequestDto;
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

    public OrderReservationController(OrderReservationService orderReservationService,
                                      OrderConverter orderConverter)
    {
        this.orderReservationService = orderReservationService;
        this.orderConverter = orderConverter;
    }

    @PostMapping
    public ResponseEntity<List<OrderDto>>getBarbershopActiveUnreservedOrdersForWeek(
            @RequestBody @Valid ShowUnreservedOrdersRequestDto showUnreservedOrdersRequestDto
    ) {
        List<Order> orders = orderReservationService.getAvailableOrders(
                showUnreservedOrdersRequestDto.barbershopId(),
                showUnreservedOrdersRequestDto.startWeekDate()
        );
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(orders), HttpStatus.OK
        );
    }

    @PostMapping("/filtered")
    public ResponseEntity<List<OrderDto>> getBarbershopActiveFilteredUnreservedOrdersForWeek(
            @RequestBody @Valid ShowUnreservedOrdersRequestDto showUnreservedOrdersRequestDto
    ) {
        List<Order> orders = orderReservationService.getFilteredAvailableOrders(
                showUnreservedOrdersRequestDto.barbershopId(),
                showUnreservedOrdersRequestDto.startWeekDate(),
                showUnreservedOrdersRequestDto.orderFilters()
        );
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(orders), HttpStatus.OK
        );
    }

    @PutMapping()
    public ResponseEntity<List<OrderDto>> reserveCustomerOrders(@RequestBody @Valid OrderReservationDto orderReservationDto) {
        List<Order> reservedOrders =
                orderReservationService.reserveCustomerOrders(
                        orderReservationDto.orderIds(),
                        orderReservationDto.customerId()
                );
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(reservedOrders),
                HttpStatus.OK
        );
    }

}
