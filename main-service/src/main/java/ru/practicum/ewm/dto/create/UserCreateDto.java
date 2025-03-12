package ru.practicum.ewm.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @NotNull
        @NotBlank
        @Email
        @Size(min = 6, max = 254)
        String email,
        @NotNull
        @NotBlank
        @Size(min = 2, max = 250)
        String name
) {
}
