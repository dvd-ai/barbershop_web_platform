package com.app.barbershopweb.order.reservation.converter;

import com.app.barbershopweb.order.reservation.dto.ShowUnreservedOrdersDto;
import com.app.barbershopweb.order.reservation.entity.ShowUnreservedOrders;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowUnreservedOrdersConverter {

    public List<ShowUnreservedOrders> showUnreservedOrdersDtoListToEntityList(List<ShowUnreservedOrdersDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<ShowUnreservedOrdersDto> showUnreservedOrdersEntityListToDtoList(List<ShowUnreservedOrders> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public ShowUnreservedOrders mapToEntity(ShowUnreservedOrdersDto dto) {
        return new ShowUnreservedOrders(
                dto.barbershopId(),
                dto.reservationDateToStartWeekFrom(),
                dto.orderFilters()
        );
    }

    public ShowUnreservedOrdersDto mapToDto(ShowUnreservedOrders entity) {
        return new ShowUnreservedOrdersDto(
                entity.getBarbershopId(),
                entity.getReservationDateToStartWeekFrom(),
                entity.getOrderFilters()

        );
    }
}
