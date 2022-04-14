package com.app.barbershopweb.order.reservation;

import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.reservation.dto.GetOpenFilteredOrdersRequestDto;
import com.app.barbershopweb.order.reservation.dto.GetOpenOrdersRequestDto;
import com.app.barbershopweb.order.reservation.dto.OrderReservationDto;
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
    public List<OrderDto> getAvailableOrders(
            @RequestBody @Valid GetOpenOrdersRequestDto getOpenOrdersRequestDto
    ) {
        List<Order> orders = orderReservationService.getAvailableOrders(
                getOpenOrdersRequestDto.barbershopId(),
                getOpenOrdersRequestDto.startWeekDate()
        );
       return orderConverter.orderEntityListToDtoList(orders);
    }

    @PostMapping("/filtered")
    public List<OrderDto> getFilteredAvailableOrders(
            @RequestBody @Valid GetOpenFilteredOrdersRequestDto getOpenFilteredOrdersRequestDto
    ) {
        List<Order> orders = orderReservationService.getFilteredAvailableOrders(
                getOpenFilteredOrdersRequestDto.barbershopId(),
                getOpenFilteredOrdersRequestDto.startWeekDate(),
                getOpenFilteredOrdersRequestDto.orderFilters()
        );
        return orderConverter.orderEntityListToDtoList(orders);
    }

    @PutMapping()
    public List<OrderDto> reserveOrders(@RequestBody @Valid OrderReservationDto orderReservationDto) {
        List<Order> reservedOrders =
                orderReservationService.reserveOrders(
                        orderReservationDto.orderIds(),
                        orderReservationDto.customerId()
                );

        return orderConverter.orderEntityListToDtoList(reservedOrders);
    }

}
