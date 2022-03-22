package com.app.barbershopweb.order.reservation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShowUnreservedOrdersConverter {

    public List<ShowUnreservedOrders> orderReservationDtoListToEntityList(List<ShowUnreservedOrdersDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<ShowUnreservedOrdersDto> orderReservationEntityListToDtoList(List<ShowUnreservedOrders> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public ShowUnreservedOrders mapToEntity(ShowUnreservedOrdersDto dto) {
        return new ShowUnreservedOrders(
                dto.barbershopId(),
                dto.reservationDateToStartWeekFrom(),
                dto.barberIds()
        );
    }

    public ShowUnreservedOrdersDto mapToDto(ShowUnreservedOrders entity) {
        return new ShowUnreservedOrdersDto(
                entity.getBarbershopId(),
                entity.getReservationDateToStartWeekFrom(),
                entity.getBarberIds()
        );
    }
}
