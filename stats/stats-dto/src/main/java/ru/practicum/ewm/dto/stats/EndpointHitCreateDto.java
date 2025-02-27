package ru.practicum.ewm.dto.stats;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EndpointHitCreateDto(
        long id,
        @NotNull
        @NotBlank
        String app,
        @NotNull
        @NotBlank
        String uri,
        String ip,
        String timestamp
) {
}
