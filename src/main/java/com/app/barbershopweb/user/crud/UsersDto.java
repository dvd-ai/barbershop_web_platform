package com.app.barbershopweb.user.crud;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UsersDto(
        @Min(1)
        Long id,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String email,
        @NotBlank
        String role,

        @NotNull
        LocalDateTime registrationDate
) {


}
