package ru.practicum.ewm.dto.response;

import java.util.List;

public record RequestEventUpdateResponseDto(
        List<RequestResponseDto> confirmedRequests,
        List<RequestResponseDto> rejectedRequests
) {
}
