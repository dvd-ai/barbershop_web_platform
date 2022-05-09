package com.app.barbershopweb.user.registration;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record UserRegistrationDto (
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String phoneNumber,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotNull
        LocalDateTime registrationDate
) {
}
