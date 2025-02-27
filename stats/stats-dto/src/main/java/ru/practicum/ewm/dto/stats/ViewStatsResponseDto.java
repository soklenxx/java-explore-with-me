package ru.practicum.ewm.dto.stats;

public record ViewStatsResponseDto(
        String app,
        String uri,
        Long hits
) {
}
