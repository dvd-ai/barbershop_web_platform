package com.app.barbershopweb.order.reservation.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public record OrderReservationDto(
        @NotEmpty
        List<Long> orderIds,
        @Min(1)
        @NotNull
        Long customerId
) {
}
