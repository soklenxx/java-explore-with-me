package ru.practicum.ewm.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.create.CommentCreateDto;
import ru.practicum.ewm.dto.update.CommentUpdateDto;
import ru.practicum.ewm.entity.Comment;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.repository.CommentRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.ewm.dto.stats.ConstantDate.DATE;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE);
    private final CommentRepository repository;
    private final UserService userService;
    private final EventService eventService;
    private final CommentMapper mapper;

    @Override
    public Comment createComment(CommentCreateDto createDto) {
        Comment comment = mapper.toEntity(createDto);
        comment.setCreatedOn(LocalDateTime.now());
        User commenter = userService.getUserById(createDto.commenterId());
        Event event = eventService.findById(createDto.eventId());
        comment.setCommenter(commenter);
        comment.setEvent(event);
        return repository.save(comment);
    }

    @Override
    public Comment patchCommentByUser(long id, CommentUpdateDto updateDto) {
        Comment comment = getCommentById(id);
        comment = mapper.toEntity(updateDto, comment);
        return repository.save(comment);
    }

    @Override
    public Comment getCommentById(long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Comment not found")
        );
    }

    @Override
    public List<Comment> getEventComments(long eventId, String start, String end, int from, int size) {
        Event event = eventService.findById(eventId);
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return repository.findByEvent(event, page).stream()
                .filter(e -> e.getCreatedOn().isAfter(startDate)
                        && e.getCreatedOn().isBefore(endDate))
                .toList();
    }

    @Override
    public void deleteComment(long id) {
        repository.deleteById(id);
    }
}
