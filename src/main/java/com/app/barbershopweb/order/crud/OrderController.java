package com.app.barbershopweb.order.crud;

import com.app.barbershopweb.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderConverter orderConverter;

    public OrderController(OrderService orderService, OrderConverter orderConverter) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        return new ResponseEntity<>(
                orderConverter.orderEntityListToDtoList(orderService.getOrders()), HttpStatus.OK
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderByOrderId(@PathVariable @Min(1) Long orderId) {
        Order order = orderService.findOrderByOrderId(orderId)
                .orElseThrow(() ->
                        new NotFoundException(
                                List.of("Order with orderId '" + orderId + "' not found.")
                        )
                );
        return new ResponseEntity<>(orderConverter.mapToDto(order), HttpStatus.OK);
    }

    @PostMapping
    //id is obligation due to @Valid
    public ResponseEntity<Long> addOrder(@RequestBody @Valid OrderDto orderDto) {
        Order entity = orderConverter.mapToEntity(orderDto);
        return new ResponseEntity<>(orderService.addOrder(entity), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@RequestBody @Valid OrderDto orderDto) {
        Order entity = orderService.updateOrder(orderConverter.mapToEntity(orderDto))
                .orElseThrow(() ->
                        new NotFoundException(
                                List.of("Order with orderId '" + orderDto.orderId() + "' not found.")
                        )
                );
        return new ResponseEntity<>(orderConverter.mapToDto(entity), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Object> deleteOrderByOrderId(@PathVariable @Min(1) Long orderId) {
        orderService.deleteOrderByOrderId(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
