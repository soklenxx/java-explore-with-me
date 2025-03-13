package ru.practicum.ewm.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.create.EventCreateDto;
import ru.practicum.ewm.dto.response.EventResponseDto;
import ru.practicum.ewm.dto.update.EventRequestUserUpdateDto;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.service.EventService;

import java.io.IOException;
import java.util.List;

import static ru.practicum.ewm.controller.URIConstants.EVENTS_ADMIN_URI;
import static ru.practicum.ewm.controller.URIConstants.EVENTS_URI;
import static ru.practicum.ewm.controller.URIConstants.EVENT_ID_PARAM;
import static ru.practicum.ewm.controller.URIConstants.ID_PARAM;
import static ru.practicum.ewm.controller.URIConstants.USERS_URI;
import static ru.practicum.ewm.controller.URIConstants.USER_ID_PARAM;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService service;
    private final EventMapper mapper;

    @GetMapping(EVENTS_ADMIN_URI)
    public List<EventResponseDto> getEventsByParams(@RequestParam(required = false) String users,
                                                    @RequestParam(required = false) String states,
                                                    @RequestParam(required = false) String categories,
                                                    @RequestParam(required = false) String rangeStart,
                                                    @RequestParam(required = false) String rangeEnd,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return service.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping(EVENTS_URI)
    public List<EventResponseDto> getEventsByText(@RequestParam(required = false) String text,
                                                  @RequestParam(required = false) String categories,
                                                  @RequestParam(required = false) Boolean paid,
                                                  @RequestParam(required = false) String rangeStart,
                                                  @RequestParam(required = false) String rangeEnd,
                                                  @RequestParam(required = false) Boolean onlyAvailable,
                                                  @RequestParam(required = false) String sort,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        return service.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping(EVENTS_URI + ID_PARAM)
    public EventResponseDto getEventById(@PathVariable long id,
                                         HttpServletRequest request) throws IOException {
        return mapper.toResponse(service.getEventById(id, request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(USERS_URI + USER_ID_PARAM + EVENTS_URI)
    public EventResponseDto createEvent(@PathVariable long userId,
                                        @RequestBody @Valid EventCreateDto eventCreateDto) {
        return mapper.toResponse(service.createEvent(userId, eventCreateDto));
    }

    @GetMapping(USERS_URI + USER_ID_PARAM + EVENTS_URI + EVENT_ID_PARAM)
    public EventResponseDto getEventInfo(@PathVariable long userId,
                                         @PathVariable long eventId) {
        return mapper.toResponse(service.getEventInfo(eventId, userId));
    }

    @PatchMapping(EVENTS_ADMIN_URI + EVENT_ID_PARAM)
    public EventResponseDto updateEvent(@PathVariable long eventId,
                                        @RequestBody @Valid EventRequestUserUpdateDto eventDto) {
        return mapper.toResponse(service.patchEvent(eventId, eventDto));
    }


    @PatchMapping(USERS_URI + USER_ID_PARAM + EVENTS_URI + EVENT_ID_PARAM)
    public EventResponseDto updateEventByCurrentUser(@PathVariable long userId,
                                                     @PathVariable long eventId,
                                                     @RequestBody @Valid EventRequestUserUpdateDto eventDto) {
        return mapper.toResponse(service.patchEventByUser(userId, eventId, eventDto));
    }

    @GetMapping(USERS_URI + USER_ID_PARAM + EVENTS_URI)
    public List<EventResponseDto> addEvent(@PathVariable long userId,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        return service.getUserEvents(userId, from, size).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
