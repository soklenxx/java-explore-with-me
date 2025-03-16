package ru.practicum.ewm.dto.create;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CategoryCreateDto(
        @NotBlank
        @Length(max = 50)
        String name
) {
}
