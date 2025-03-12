package ru.practicum.ewm.dto.response;

import ru.practicum.ewm.entity.EventState;

public record RequestResponseDto(
        long id,
        String created,
        long event,
        long requester,
        EventState status
) {
}
