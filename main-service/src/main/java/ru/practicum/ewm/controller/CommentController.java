package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.create.CommentCreateDto;
import ru.practicum.ewm.dto.response.CommentResponseDto;
import ru.practicum.ewm.dto.update.CommentUpdateDto;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.service.CommentService;
import java.util.List;

import static ru.practicum.ewm.controller.URIConstants.COMMENTS_ID_PARAM;
import static ru.practicum.ewm.controller.URIConstants.COMMENTS_URI;
import static ru.practicum.ewm.controller.URIConstants.EVENTS_URI;
import static ru.practicum.ewm.controller.URIConstants.EVENT_ID_PARAM;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;
    private final CommentMapper mapper;

    @PostMapping(COMMENTS_URI)
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@RequestBody @Valid CommentCreateDto createDto) {
        return mapper.toResponse(service.createComment(createDto));
    }

    @PatchMapping(COMMENTS_URI + COMMENTS_ID_PARAM)
    public CommentResponseDto patchCommentByUser(@PathVariable long commentId,
                                                 @RequestBody @Valid CommentUpdateDto updateDto) {
        return mapper.toResponse(service.patchCommentByUser(commentId, updateDto));
    }

    @GetMapping(COMMENTS_URI + COMMENTS_ID_PARAM)
    public CommentResponseDto getCommentById(@PathVariable long commentId) {
        return mapper.toResponse(service.getCommentById(commentId));
    }

    @GetMapping(COMMENTS_URI + EVENTS_URI + EVENT_ID_PARAM)
    public List<CommentResponseDto> getEventComments(@PathVariable long eventId,
                                                     @RequestParam(defaultValue = "2025-01-01 00:00:00") String start,
                                                     @RequestParam(defaultValue = "2055-01-01 00:00:00") String end,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        return service.getEventComments(eventId, start, end, from, size).stream().map(mapper::toResponse).toList();
    }

    @DeleteMapping(COMMENTS_URI + COMMENTS_ID_PARAM)
    public void deleteComment(@PathVariable long commentId) {
        service.deleteComment(commentId);
    }

}
