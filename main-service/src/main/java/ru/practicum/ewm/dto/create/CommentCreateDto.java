package ru.practicum.ewm.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateDto (
        @NotNull
        @NotBlank
        String text,
        String created,
        Long commenterId,
        Long eventId
) {
}
