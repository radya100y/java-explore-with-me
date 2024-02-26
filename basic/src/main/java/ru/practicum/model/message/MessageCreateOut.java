package ru.practicum.model.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.practicum.model.category.CategoryOut;
import ru.practicum.model.user.UserOutWithoutEmail;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class MessageCreateOut {

    private Long id;
    private String annotation;
    private CategoryOut category;
    private String description;
    private UserOutWithoutEmail initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private MessageStatus state;
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime publishedOn;

    private Integer views;
    private Integer confirmedRequests;
}