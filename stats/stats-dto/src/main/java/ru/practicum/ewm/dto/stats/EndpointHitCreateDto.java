package ru.practicum.ewm.dto.stats;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
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
