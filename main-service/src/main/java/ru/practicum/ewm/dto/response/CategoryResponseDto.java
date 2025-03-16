package ru.practicum.ewm.dto.response;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CategoryResponseDto(
        long id,
        @NotBlank
        @Length(max = 50)
        String name
) {
}
