package ru.practicum.ewm.dto.create;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateDto(
        @NotBlank
        String text,
        String created,
        Long commenterId,
        Long eventId
) {
}
