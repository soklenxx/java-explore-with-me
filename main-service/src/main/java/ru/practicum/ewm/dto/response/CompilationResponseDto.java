package ru.practicum.ewm.dto.response;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record CompilationResponseDto(
        long id,
        Boolean pinned,
        @NotBlank
        @Length(max = 50)
        String title,
        Set<EventResponseDto> events
) {
}
