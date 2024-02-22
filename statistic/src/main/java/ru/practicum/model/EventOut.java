package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class EventOut {

    private long id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime dt;
}
