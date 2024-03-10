package ru.practicum.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public class RequestOut {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;

    private long event;

    private long id;

    private long requester;

    private RequestStatus status;
}
