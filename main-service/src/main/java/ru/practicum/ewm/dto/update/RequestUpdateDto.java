package ru.practicum.ewm.dto.update;

import java.util.List;

public record RequestUpdateDto(
        List<Long> requestIds,
        String status
) {
}
