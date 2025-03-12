package ru.practicum.ewm.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.practicum.ewm.dto.info.LocationInfoDto;

public record EventCreateDto(
        long id,
        @NotNull
        @NotBlank
        @Size(min = 20, message = "{validation.annotation.size.too_short}")
        @Size(max = 2000, message = "{validation.annotation.size.too_long}")
        String annotation,
        @JsonProperty("category")
        Long categoryId,
        @NotNull
        @NotBlank
        @Size(min = 20, message = "{validation.description.size.too_short}")
        String description,
        String eventDate,
        LocationInfoDto location,
        Boolean paid,
        @Min(0)
        Integer participantLimit,
        Boolean requestModeration,
        @Size(min = 3, message = "{validation.title.size.too_short}")
        @Size(max = 120, message = "{validation.title.size.too_long}")
        String title
) {
}
