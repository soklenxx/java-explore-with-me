package ru.practicum.ewm.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.ewm.dto.create.EventCreateDto;
import ru.practicum.ewm.dto.response.EventResponseDto;
import ru.practicum.ewm.dto.update.EventRequestUserUpdateDto;
import ru.practicum.ewm.entity.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewm.dto.stats.ConstantDate.DATE;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "eventDate", ignore = true)
    @Mapping(target = "confirmedRequests", expression = "java(0)")
    Event toEntity(EventCreateDto eventCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "eventDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event toEntity(EventRequestUserUpdateDto eventUpdateDto, @MappingTarget Event event);

    @Mapping(target = "eventDate", expression = "java(dateToString(event))")
    EventResponseDto toResponse(Event event);

    default String dateToString(Event event) {
        LocalDateTime eventDate = event.getEventDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE);
        return eventDate != null ? eventDate.format(formatter) : null;
    }
}
