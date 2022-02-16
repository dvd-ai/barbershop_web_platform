package com.app.barbershopweb.barbershop;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record BarbershopDto(
        @Min(1) Long id,
        @NotBlank String address,
        @NotBlank String name,
        @NotBlank String phoneNumber,
        @NotBlank String email
) {
}
