package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.response.RequestResponseDto;
import ru.practicum.ewm.entity.Request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewm.dto.stats.ConstantDate.DATE;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    @Mapping(target = "created", expression = "java(dateToString(request))")
    RequestResponseDto toResponse(Request request);

    default String dateToString(Request request) {
        LocalDateTime eventDate = request.getCreated();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE);
        return eventDate != null ? eventDate.format(formatter) : null;
    }
}
