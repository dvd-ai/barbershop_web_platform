package com.app.barbershopweb.order.reservation.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public record OrderFiltersDto(
        @NotEmpty
        List<Long>barberIds
) {
}
