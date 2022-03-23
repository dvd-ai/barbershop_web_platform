package com.app.barbershopweb.order.reservation.dto;

import com.app.barbershopweb.order.reservation.entity.OrderFilters;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


public record ShowUnreservedOrdersDto(
        @Min(1)
        @NotNull
        Long barbershopId,
        @NotNull
        LocalDateTime reservationDateToStartWeekFrom,
        @NotNull
        OrderFilters orderFilters
) {
}
