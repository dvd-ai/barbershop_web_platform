package com.app.barbershopweb.order.reservation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record ShowUnreservedOrdersRequestDto(
        @Min(1)
        @NotNull
        Long barbershopId,
        @NotNull
        LocalDateTime reservationDateToStartWeekFrom,
        @NotNull
        List<Long>barberIds
) {
}
