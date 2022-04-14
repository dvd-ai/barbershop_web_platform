package com.app.barbershopweb.order.reservation;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.reservation.dto.GetOpenFilteredOrdersRequestDto;
import com.app.barbershopweb.order.reservation.dto.GetOpenOrdersRequestDto;
import com.app.barbershopweb.order.reservation.dto.OrderReservationDto;
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
                                      OrderConverter orderConverter) {
        this.orderReservationService = orderReservationService;
        this.orderConverter = orderConverter;
    }

    @PostMapping
    public ResponseEntity<List<OrderDto>> getAvailableOrders(
            @RequestBody @Valid GetOpenOrdersRequestDto getOpenOrdersRequestDto
    ) {
        List<Order> orders = orderReservationService.getAvailableOrders(
                getOpenOrdersRequestDto.barbershopId(),
                getOpenOrdersRequestDto.startWeekDate()
        );
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(orders), HttpStatus.OK
        );
    }

    @PostMapping("/filtered")
    public ResponseEntity<List<OrderDto>> getFilteredAvailableOrders(
            @RequestBody @Valid GetOpenFilteredOrdersRequestDto getOpenOrdersRequestDto
    ) {
        List<Order> orders = orderReservationService.getFilteredAvailableOrders(
                getOpenOrdersRequestDto.barbershopId(),
                getOpenOrdersRequestDto.startWeekDate(),
                getOpenOrdersRequestDto.orderFilters()
        );
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(orders), HttpStatus.OK
        );
    }

    @PutMapping()
    public ResponseEntity<List<OrderDto>> reserveOrders(@RequestBody @Valid OrderReservationDto orderReservationDto) {
        List<Order> reservedOrders =
                orderReservationService.reserveOrders(
                        orderReservationDto.orderIds(),
                        orderReservationDto.customerId()
                );
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(reservedOrders),
                HttpStatus.OK
        );
    }

}
