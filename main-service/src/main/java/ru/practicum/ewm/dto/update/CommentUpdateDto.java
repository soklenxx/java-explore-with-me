package ru.practicum.ewm.dto.update;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateDto(
        Long id,
        @NotBlank
        String text,
        String created,
        Long commenterId,
        Long eventId
) {
}
