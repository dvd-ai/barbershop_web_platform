package com.app.barbershopweb.order.reservation.converter;

import com.app.barbershopweb.order.reservation.dto.OrderFiltersDto;
import com.app.barbershopweb.order.reservation.entity.OrderFilters;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFiltersConverter {
    public List<OrderFilters> orderFiltersDtoListToEntityList(List<OrderFiltersDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<OrderFiltersDto> orderFiltersEntityListToDtoList(List<OrderFilters> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public OrderFilters mapToEntity(OrderFiltersDto dto) {
        return new OrderFilters(
                dto.barberIds()
        );
    }

    public OrderFiltersDto mapToDto(OrderFilters entity) {
        return new OrderFiltersDto(
                entity.getBarberIds()
        );
    }
}
