package ru.practicum.ewm.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record CompilationCreateDto(
        long id,
        Boolean pinned,
        @NotNull
        @NotBlank
        @Length(max = 50)
        String title,
        Set<Long> events
) {
}
