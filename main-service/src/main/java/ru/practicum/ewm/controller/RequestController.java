package ru.practicum.ewm.controller;

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
import ru.practicum.ewm.dto.response.RequestEventUpdateResponseDto;
import ru.practicum.ewm.dto.response.RequestResponseDto;
import ru.practicum.ewm.dto.update.RequestUpdateDto;
import ru.practicum.ewm.exception.ArgumentResolverException;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.service.RequestService;

import java.util.List;

import static ru.practicum.ewm.controller.URIConstants.CANCEL_URI;
import static ru.practicum.ewm.controller.URIConstants.EVENTS_URI;
import static ru.practicum.ewm.controller.URIConstants.EVENT_ID_PARAM;
import static ru.practicum.ewm.controller.URIConstants.REQUESTS_URI;
import static ru.practicum.ewm.controller.URIConstants.REQUEST_ID_PARAM;
import static ru.practicum.ewm.controller.URIConstants.USERS_URI;
import static ru.practicum.ewm.controller.URIConstants.USER_ID_PARAM;

@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService service;
    private final RequestMapper mapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(USERS_URI + USER_ID_PARAM + REQUESTS_URI)
    public RequestResponseDto createRequest(@PathVariable Long userId,
                                            @Valid @RequestParam(required = false, defaultValue = "0") Long eventId) {
        if (eventId == 0) {
            throw new ArgumentResolverException("event_id is null");
        }
        return mapper.toResponse(service.createRequest(userId, eventId));
    }

    @PatchMapping(USERS_URI + USER_ID_PARAM + EVENTS_URI + EVENT_ID_PARAM + REQUESTS_URI)
    public RequestEventUpdateResponseDto patchRequestStatus(@PathVariable Long userId,
                                                            @PathVariable Long eventId,
                                                            @RequestBody RequestUpdateDto request) {
        return service.patchRequestStatus(userId, eventId, request);
    }

    @GetMapping(USERS_URI + USER_ID_PARAM + REQUESTS_URI)
    public List<RequestResponseDto> getUsersRequests(@PathVariable Long userId) {
        return service.getUsersRequests(userId).stream().map(mapper::toResponse).toList();
    }

    @GetMapping(USERS_URI + USER_ID_PARAM + EVENTS_URI + EVENT_ID_PARAM + REQUESTS_URI)
    public List<RequestResponseDto> getCurrentUsersRequests(@PathVariable Long userId,
                                                            @PathVariable Long eventId) {
        return service.getCurrentUsersRequests(userId, eventId).stream().map(mapper::toResponse).toList();
    }

    @PatchMapping(USERS_URI + USER_ID_PARAM + REQUESTS_URI + REQUEST_ID_PARAM + CANCEL_URI)
    public RequestResponseDto canceledRequest(@PathVariable Long userId,
                                              @PathVariable Long requestId) {
        return mapper.toResponse(service.canceledRequest(userId, requestId));
    }
}
