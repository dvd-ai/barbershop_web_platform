package com.app.barbershopweb.order.crud;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record OrderDto(
     @Min(1)
     Long orderId,
     @Min(1)
     Long barbershopId,
     @Min(1)
     Long barberId,
     @Min(1)
     Long customerId,
     @NotNull
     LocalDateTime orderDate,
     @NotNull
     Boolean active
) {
    
    
}
