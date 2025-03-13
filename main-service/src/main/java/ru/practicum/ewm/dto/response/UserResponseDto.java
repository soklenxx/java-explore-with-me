package ru.practicum.ewm.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record UserResponseDto(
        long id,
        @NotNull
        @NotBlank
        @Length(min = 6, max = 254)
        String email,
        @NotNull
        @NotBlank
        @Size(min = 2)
        @Size(max = 250)
        String name
) {
}
