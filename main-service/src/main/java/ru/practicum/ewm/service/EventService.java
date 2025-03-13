package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.dto.create.EventCreateDto;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.dto.update.EventRequestUserUpdateDto;

import java.io.IOException;
import java.util.List;

public interface EventService {
    List<Event> getEvents(
            String text, String categories, Boolean paid, String rangeStart, String rangeEnd, Boolean onlyAvailable,
            String sort, int from, int size, HttpServletRequest request);

    Event createEvent(long userId, EventCreateDto event);

    List<Event> searchEvents(String users, String states, String categories, String rangeStart, String rangeEnd, int from, int size);

    Event getEventInfo(long eventId, long userId);

    Event getEventById(long eventId, HttpServletRequest request) throws IOException;

    Event patchEvent(long eventId, EventRequestUserUpdateDto updateDto);

    Event patchEventByUser(long userId, long eventId, EventRequestUserUpdateDto updateDto);

    List<Event> getUserEvents(long userId, int from, int size);

    Event findById(long eventId);

}
