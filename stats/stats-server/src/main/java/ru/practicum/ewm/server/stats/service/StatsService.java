package ru.practicum.ewm.server.stats.service;

import ru.practicum.ewm.dto.stats.EndpointHitCreateDto;
import ru.practicum.ewm.dto.stats.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveHit(EndpointHitCreateDto endpointHitCreateDto);

    List<ViewStatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
