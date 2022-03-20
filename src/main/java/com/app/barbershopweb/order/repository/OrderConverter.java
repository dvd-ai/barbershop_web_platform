package com.app.barbershopweb.order.repository;

import com.app.barbershopweb.order.Order;
import com.app.barbershopweb.order.OrderDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderConverter {

    public List<Order> orderDtoListToEntityList(List<OrderDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<OrderDto> orderEntityListToDtoList(List<Order> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public Order mapToEntity(OrderDto dto) {
        return new Order(
                dto.orderId(),
                dto.barbershopId(),
                dto.barberId(),
                dto.customerId(),
                dto.orderDate(),
                dto.active()
        );
    }

    public OrderDto mapToDto(Order entity) {
        return new OrderDto(
                entity.getOrderId(),
                entity.getBarbershopId(),
                entity.getBarberId(),
                entity.getCustomerId(),
                entity.getOrderDate(),
                entity.getActive()
        );
    }
}
