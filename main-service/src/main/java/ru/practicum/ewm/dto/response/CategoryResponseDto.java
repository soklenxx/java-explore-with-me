package ru.practicum.ewm.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CategoryResponseDto(
        long id,
        @NotNull
        @NotBlank
        @Length(max = 50)
        String name
) {
}
