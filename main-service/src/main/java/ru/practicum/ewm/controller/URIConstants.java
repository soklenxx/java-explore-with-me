package ru.practicum.ewm.controller;

public interface URIConstants {

    //---URI---//

    String CATEGORIES_ADMIN_URI = "/admin/categories";
    String CATEGORIES_URI = "/categories";
    String USERS_ADMIN_URI = "/admin/users";
    String ADMIN_URI = "/admin";
    String USERS_URI = "/users";
    String EVENTS_ADMIN_URI = "/admin/events";
    String EVENTS_URI = "/events";
    String REQUESTS_URI = "/requests";
    String CANCEL_URI = "/cancel";
    String COMPILATIONS_URI = "/compilations";
    String COMMENTS_URI = "/comments";


    //---PARAMS---//

    String ID_PARAM = "/{id}";
    String USER_ID_PARAM = "/{userId}";
    String EVENT_ID_PARAM = "/{eventId}";
    String REQUEST_ID_PARAM = "/{requestId}";
    String COMPILATIONS_ID_PARAM = "/{compId}";
    String COMMENTS_ID_PARAM = "/{commentId}";
}
