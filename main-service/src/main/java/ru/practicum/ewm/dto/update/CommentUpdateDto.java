package ru.practicum.ewm.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentUpdateDto(
        Long id,
        @NotNull
        @NotBlank
        String text,
        String created,
        Long commenterId,
        Long eventId
) {
}
