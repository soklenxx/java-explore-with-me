package ru.practicum.ewm.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import ru.practicum.ewm.dto.info.LocationInfoDto;

public record EventRequestUserUpdateDto(
        @Size(min = 20, message = "{validation.annotation.size.too_short}")
        @Size(max = 2000, message = "{validation.annotation.size.too_long}")
        String annotation,
        @JsonProperty("category")
        Long categoryId,
        @Size(max = 7000, message = "{validation.description.size.too_long}")
        @Size(min = 20, message = "{validation.description.size.too_short}")
        String description,
        String eventDate,
        LocationInfoDto location,
        Boolean paid,
        @Min(0)
        Integer participantLimit,
        Boolean requestModeration,
        String stateAction,
        @Size(min = 3, message = "{validation.title.size.too_short}")
        @Size(max = 120, message = "{validation.title.size.too_long}")
        String title
) {
}
