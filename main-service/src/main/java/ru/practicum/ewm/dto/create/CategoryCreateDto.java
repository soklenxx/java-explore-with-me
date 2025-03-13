package ru.practicum.ewm.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CategoryCreateDto(
        long id,
        @NotNull
        @NotBlank
        @Length(max = 50)
        String name
) {
}
