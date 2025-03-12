package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.create.EventCreateDto;
import ru.practicum.ewm.dto.stats.EndpointHitCreateDto;
import ru.practicum.ewm.dto.stats.ViewStatsResponseDto;
import ru.practicum.ewm.dto.update.EventRequestUserUpdateDto;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.EventState;
import ru.practicum.ewm.entity.StateAction;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.ArgumentResolverException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.repository.EventRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.dto.stats.ConstantDate.DATE;
import static ru.practicum.ewm.service.UserServiceImpl.parseIds;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE);
    private final EventRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final StatsClient statsClient;
    private final EventMapper mapper;

    @Override
    @Transactional
    public List<Event> searchEvents(String users, String states, String categories, String rangeStart, String rangeEnd, int from, int size) {
        PageRequest page = checkPageableParameters(from, size);
        List<Long> usersList = parseIds(users);
        List<Long> categoriesList = parseIds(categories);
        List<EventState> statesList = parseStates(states);

        List<Event> events = repository.findAllByStatesAndUsersAndCategories(statesList, usersList, categoriesList, page);

        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
            return events.stream()
                    .filter(e -> e.getEventDate().isAfter(start)
                            && e.getEventDate().isBefore(end))
                    .toList();
        }
        return events;
    }

    @Override
    @Transactional
    public List<Event> getEvents(
            String text, String categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable,
            String sort, int from, int size, HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, formatter);
            if (!end.isAfter(start)) {
                throw new ArgumentResolverException("Event must be published");
            }
        }
        saveStats(request);
        List<Event> events = repository.findAll();
        if (text != null) {
            events = events.stream()
                    .filter(event -> event.getAnnotation().contains(text) || event.getDescription().contains(text)).collect(Collectors.toList());
        }
        if (paid != null && paid) {
            events = events.stream()
                    .filter(Event::getPaid).collect(Collectors.toList());
        }
        if (sort != null && sort.equals("EVENT_DATE")) {
            return events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
        }
        if (sort != null && sort.equals("VIEWS")) {
            return events.stream()
                    .sorted(Comparator.comparingLong(Event::getViews)).collect(Collectors.toList());
        }
        return events.stream().limit(size).collect(Collectors.toList());
    }

    @Override
    public Event createEvent(long userId, EventCreateDto eventCreateDto) {
        Event event = mapper.toEntity(eventCreateDto);
        LocalDateTime eventDate = LocalDateTime.parse(eventCreateDto.eventDate(), formatter);
        LocalDateTime currentDate = LocalDateTime.now();
        long duration = Duration.between(currentDate, eventDate).getSeconds();
        if (duration < 7200) {
            throw new ArgumentResolverException(
                    "event date and time cannot be earlier than two hours from the current moment");
        }
        User initiator = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(eventCreateDto.categoryId());
        event.setEventDate(eventDate);
        event.setCreatedOn(currentDate);
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        if (eventCreateDto.paid() == null) {
            event.setPaid(false);
        }
        if (eventCreateDto.participantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (eventCreateDto.requestModeration() == null) {
            event.setRequestModeration(true);
        }
        return repository.save(event);
    }

    @Override
    @Transactional
    public Event getEventInfo(long eventId, long userId) {
        return repository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new NotFoundException("Event with id " + eventId + " not found")
        );
    }


    @Override
    @Transactional
    public Event getEventById(long eventId, HttpServletRequest request) {
        checkExists(eventId);
        saveStats(request);
        Event event = findById(eventId);
        List<ViewStatsResponseDto> response = statsClient.getStats("2020-01-01 00:00:00", "2035-01-01 00:00:00", request.getRequestURI(), true);
        event.setViews(response.getFirst().hits());
        return repository.save(event);
    }


    @Override
    @Transactional
    public Event patchEvent(long eventId, EventRequestUserUpdateDto eventRequest) {
        Event event = findById(eventId);
        EventState state = event.getState();
        String stateAction = eventRequest.stateAction();
        if (state.equals(EventState.CANCELED)) {
            throw new ConflictException("Event with id " + eventId + " already " + state);
        }
        if (state.equals(EventState.PENDING) || !state.equals(EventState.PUBLISHED)) {
            event = updateEventRequest(event, eventRequest);
            if (stateAction != null && StateAction.valueOf(stateAction).equals(StateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            }
            if (stateAction != null && StateAction.valueOf(stateAction).equals(StateAction.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        } else {
            throw new ConflictException("Event with id " + eventId + " already " + state);
        }
        return repository.save(event);
    }

    @Override
    @Transactional
    public Event patchEventByUser(long userId, long eventId, EventRequestUserUpdateDto eventRequest) {
        Event event = getEventInfo(eventId, userId);
        EventState oldEventState = event.getState();
        String stateAction = eventRequest.stateAction();
        Long initiatorId = event.getInitiator().getId();
        if (initiatorId.equals(userId) && event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Modifying a published event on behalf of a user");
        }
        if (oldEventState.equals(EventState.CANCELED) || oldEventState.equals(EventState.PENDING)) {
            event = updateEventRequest(event, eventRequest);
            if (eventRequest.eventDate() != null) {
                event.setState(EventState.valueOf(stateAction));
            }
            if (stateAction != null && StateAction.valueOf(stateAction).equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            }
            if (stateAction != null && StateAction.valueOf(stateAction).equals(StateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        } else {
            throw new ArgumentResolverException("Imposible to modify status of event ");
        }
        return repository.save(event);
    }

    @Override
    @Transactional
    public List<Event> getUserEvents(long userId, int from, int size) {
        PageRequest page = checkPageableParameters(from, size);
        return repository.findEventsByInitiatorId(userId, page);
    }

    @Override
    @Transactional
    public Event findById(long eventId) {
        return repository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id " + eventId + " not found"));
    }

    private PageRequest checkPageableParameters(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new RuntimeException("Параметр from не должен быть меньше 1");
        }
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    private List<EventState> parseStates(@NotNull String states) {
        return states != null
                ? Arrays.stream(states.split(","))
                .map(EventState::valueOf)
                .toList()
                : null;
    }

    private void saveStats(HttpServletRequest request) {
        EndpointHitCreateDto endpointHitDto = new EndpointHitCreateDto(
                0L, "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(formatter));
        statsClient.addStats(endpointHitDto);
    }

    private void checkExists(long eventId) {
        if (!repository.existsById(eventId)
                || repository.findById(eventId).get().getState().equals(EventState.PENDING)
                || repository.findById(eventId).get().getState().equals(EventState.CANCELED)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
    }

    private Event updateEventRequest(Event oldEvent, EventRequestUserUpdateDto updateEventRequest) {
        Event event = mapper.toEntity(updateEventRequest, oldEvent);

        if (updateEventRequest.categoryId() != null) {
            Category category = categoryService.getCategoryById(updateEventRequest.categoryId());
            event.setCategory(category);
        }

        if (updateEventRequest.eventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEventRequest.eventDate(), formatter);
            LocalDateTime currentDate = LocalDateTime.now();
            if (eventDate.isBefore(currentDate)) {
                throw new ArgumentResolverException("date cannot be in the past");
            }
            event.setEventDate(eventDate);
            event.setCreatedOn(currentDate);
        }

        return event;
    }

}
