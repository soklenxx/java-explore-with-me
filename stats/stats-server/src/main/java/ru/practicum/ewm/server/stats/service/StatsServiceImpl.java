package ru.practicum.ewm.server.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHitCreateDto;
import ru.practicum.ewm.dto.stats.ViewStatsResponseDto;
import ru.practicum.ewm.server.stats.mapper.EndpointHitMapper;
import ru.practicum.ewm.server.stats.mapper.ViewStatsMapper;
import ru.practicum.ewm.server.stats.repository.StatsRepository;
import ru.practicum.ewm.server.stats.exception.ArgumentResolverException;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsServerRepository;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @Override
    public void saveHit(EndpointHitCreateDto endpointHitCreateDto) {
        statsServerRepository.save(endpointHitMapper.toEntity(endpointHitCreateDto));
    }

    @Override
    public List<ViewStatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new ArgumentResolverException("Start date cannot be after end date");
        }
        if (unique) {
            return statsServerRepository.findDistinctViews(start, end, uris).stream().map(viewStatsMapper::toViewStatsDto).toList();
        } else {
            return statsServerRepository.findViews(start, end, uris).stream().map(viewStatsMapper::toViewStatsDto).toList();
        }
    }
}
