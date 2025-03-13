package ru.practicum.ewm.service;


import ru.practicum.ewm.dto.response.RequestEventUpdateResponseDto;
import ru.practicum.ewm.dto.update.RequestUpdateDto;
import ru.practicum.ewm.entity.Request;

import java.util.List;

public interface RequestService {
    Request createRequest(long userId, Long eventId);

    RequestEventUpdateResponseDto patchRequestStatus(long userId, long eventId, RequestUpdateDto request);

    List<Request> getUsersRequests(long userId);

    List<Request> getCurrentUsersRequests(long userId, long eventId);

    Request canceledRequest(long userId, long requestId);
}
