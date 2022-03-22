package com.app.barbershopweb.order.reservation.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public record OrderFiltersDto(
        @NotNull
        List<Long>barberIds
) {
}
