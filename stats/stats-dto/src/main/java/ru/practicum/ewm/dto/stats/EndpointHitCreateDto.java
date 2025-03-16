package ru.practicum.ewm.dto.stats;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record EndpointHitCreateDto(
        long id,
        @NotBlank
        String app,
        @NotBlank
        String uri,
        String ip,
        String timestamp
) {
}
