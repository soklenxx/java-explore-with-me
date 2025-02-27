package ru.practicum.ewm.server.stats.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.stats.ViewStatsResponseDto;
import ru.practicum.ewm.server.stats.entity.ViewStats;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {
    ViewStatsResponseDto toViewStatsDto(ViewStats viewStats);
}