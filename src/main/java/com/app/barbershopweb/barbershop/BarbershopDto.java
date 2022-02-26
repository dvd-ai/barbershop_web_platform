package com.app.barbershopweb.barbershop;


import javax.validation.constraints.*;
import java.time.LocalTime;

public record BarbershopDto(
        @Min(1) Long id,
        @NotBlank String address,
        @NotBlank String name,
        @NotBlank String phoneNumber,
        @Email @NotNull String email,

        @NotNull
        LocalTime workTimeFrom,

        @NotNull
        LocalTime workTimeTo
) {
}
