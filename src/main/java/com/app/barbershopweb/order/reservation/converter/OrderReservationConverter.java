package com.app.barbershopweb.order.reservation.converter;

import com.app.barbershopweb.order.reservation.dto.OrderReservationDto;
import com.app.barbershopweb.order.reservation.entity.OrderReservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderReservationConverter {
    public List<OrderReservation> orderReservationDtoListToEntityList(List<OrderReservationDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<OrderReservationDto> orderReservationEntityListToDtoList(List<OrderReservation> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public OrderReservation mapToEntity(OrderReservationDto dto) {
        return new OrderReservation(
                dto.orderIds(),
                dto.customerId()
        );
    }

    public OrderReservationDto mapToDto(OrderReservation entity) {
        return new OrderReservationDto(
                entity.getOrderIds(),
                entity.getCustomerId()
        );
    }
}
