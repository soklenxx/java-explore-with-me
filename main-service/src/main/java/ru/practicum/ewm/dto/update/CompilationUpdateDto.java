package ru.practicum.ewm.dto.update;

import jakarta.validation.constraints.Size;

import java.util.Set;

public record CompilationUpdateDto(
        Boolean pinned,
        @Size(min = 1, max = 50)
        String title,
        Set<Long> events
) {
}
