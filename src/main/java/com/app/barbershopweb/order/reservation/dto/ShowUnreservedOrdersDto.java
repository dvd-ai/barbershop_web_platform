package com.app.barbershopweb.order.reservation.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


public record ShowUnreservedOrdersDto(
        @Min(1)
        @NotNull
        Long barbershopId,
        @NotNull
        LocalDateTime reservationDateToStartWeekFrom
) {
}
