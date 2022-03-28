package com.app.barbershopweb.order.reservation.converter;

import com.app.barbershopweb.order.reservation.dto.ShowUnreservedOrdersRequestDto;
import com.app.barbershopweb.order.reservation.entity.ShowUnreservedOrders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowUnreservedOrdersConverter {

    public List<ShowUnreservedOrders> showUnreservedOrdersDtoListToEntityList(List<ShowUnreservedOrdersRequestDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<ShowUnreservedOrdersRequestDto> showUnreservedOrdersEntityListToDtoList(List<ShowUnreservedOrders> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public ShowUnreservedOrders mapToEntity(ShowUnreservedOrdersRequestDto dto) {
        return new ShowUnreservedOrders(
                dto.barbershopId(),
                dto.startWeekDate(),
                dto.orderFilters()
        );
    }

    public ShowUnreservedOrdersRequestDto mapToDto(ShowUnreservedOrders entity) {
        return new ShowUnreservedOrdersRequestDto(
                entity.getBarbershopId(),
                entity.getStartWeekDate(),
                entity.getOrderFilters()

        );
    }
}
