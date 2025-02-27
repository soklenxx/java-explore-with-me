package ru.practicum.ewm.server.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.stats.EndpointHitCreateDto;
import ru.practicum.ewm.server.stats.entity.EndpointHit;

import static ru.practicum.ewm.server.stats.ConstantDate.DATE;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {
    @Mapping(target = "timestamp", source = "timestamp", dateFormat = DATE)
    EndpointHit toEntity(EndpointHitCreateDto endpointHitCreateDto);
}