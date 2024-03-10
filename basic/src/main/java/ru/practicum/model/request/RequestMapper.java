package ru.practicum.model.request;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestMapper {

    public static RequestOut toRequestOut(Request request) {
        return RequestOut.builder()
                .created(request.getCreatedOn())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
