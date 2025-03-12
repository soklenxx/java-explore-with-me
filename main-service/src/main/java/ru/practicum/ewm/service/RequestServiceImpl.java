package ru.practicum.ewm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.response.RequestEventUpdateResponseDto;
import ru.practicum.ewm.dto.response.RequestResponseDto;
import ru.practicum.ewm.dto.update.RequestUpdateDto;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.EventState;
import ru.practicum.ewm.entity.Request;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.ArgumentResolverException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final UserService userService;
    private final RequestMapper mapper;

    @Override
    @Transactional
    public Request createRequest(long userId, Long eventId) {
        if (eventId == 0) {
            throw new ArgumentResolverException("event Id is null");
        }
        Event event = eventService.findById(eventId);
        checkRequest(userId, event);
        User user = userService.getUserById(userId);
        int confirmedRequestsCount = event.getConfirmedRequests();
        int participantLimit = event.getParticipantLimit();
        if (confirmedRequestsCount == participantLimit && participantLimit != 0) {
            throw new ConflictException("Adding a request to participate in an event that has reached its participant limit");
        }
        LocalDateTime now = LocalDateTime.now();
        Request request = new Request();
        request.setCreated(now);
        request.setEvent(event);
        request.setRequester(user);

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(EventState.CONFIRMED);
            confirmedRequestsCount++;
            event.setConfirmedRequests(confirmedRequestsCount);
        } else request.setStatus(EventState.PENDING);
        return repository.save(request);
    }


    @Override
    @Transactional
    public RequestEventUpdateResponseDto patchRequestStatus(long userId, long eventId, RequestUpdateDto requestUpdateDto) {
        List<Long> requestsIds = requestUpdateDto.requestIds();
        String patchedStatus = requestUpdateDto.status();
        List<RequestResponseDto> confirmedRequests = new ArrayList<>();
        List<RequestResponseDto> rejectedRequests = new ArrayList<>();
        for (Long requestsId : requestsIds) {
            Request request = findById(requestsId);
            request.setStatus(EventState.valueOf(patchedStatus));
            repository.save(request);
            Event event = eventService.getEventInfo(eventId, userId);
            int confirmedRequestsCount = event.getConfirmedRequests();
            int participantLimit = event.getParticipantLimit();
            if (confirmedRequestsCount == participantLimit && participantLimit != 0) {
                throw new ConflictException("Adding a request to participate in an event that has reached its participant limit");
            }
            confirmedRequestsCount++;
            event.setConfirmedRequests(confirmedRequestsCount);
            eventRepository.save(event);

            if (EventState.valueOf(patchedStatus).equals(EventState.CONFIRMED)) {
                confirmedRequests.add(mapper.toResponse(request));
            } else {
                rejectedRequests.add(mapper.toResponse(request));
            }
        }
        return new RequestEventUpdateResponseDto(confirmedRequests, rejectedRequests);
    }

    @Override
    @Transactional
    public List<Request> getUsersRequests(long userId) {
        return repository.findByRequesterId(userId);
    }

    @Override
    @Transactional
    public List<Request> getCurrentUsersRequests(long userId, long eventId) {
        return repository.findByEventId(eventId);
    }

    @Override
    @Transactional
    public Request canceledRequest(long userId, long requestId) {
        Request request = findById(requestId);
        request.setStatus(EventState.CANCELED);
        return request;
    }

    private Request findById(long requestId) {
        return repository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found"));
    }

    private Optional<Request> findByEventIdAndRequesterId(long eventId, long userId) {
        return repository.findByEventIdAndRequesterId(eventId, userId);
    }

    private void checkRequest(long userId, Event event) {
        if (findByEventIdAndRequesterId(event.getId(), userId).isPresent()) {
            throw new ConflictException("Request already exists");
        }

        Long initiatorId = event.getInitiator().getId();
        if (initiatorId.equals(userId)) {
            throw new ConflictException("Adding a request from the event initiator to participate in it");
        }

        if(!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Adding a request for an event with the status NOT PUBLISHED");
        }
    }
}
