package ru.practicum.model.message;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class MessageCreateIn {

    @Size(max = 256)
    private String annotation;

    @NotNull
    private long category;

    @Size(max = 1024)
    private String description;


    private LocalDateTime eventDate;

    private Location location;

    @NotNull
    private Boolean paid;

    @NotNull
    @Positive
    private Integer participantLimit;

    @NotNull
    private Boolean requestModeration;

    @Size(max = 512)
    private String title;
}
