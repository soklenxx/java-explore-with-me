package ru.practicum.ewm.dto.create;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record CompilationCreateDto(
        Boolean pinned,
        @NotBlank
        @Length(max = 50)
        String title,
        Set<Long> events
) {
}
