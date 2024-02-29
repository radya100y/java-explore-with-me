package ru.practicum.model.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.practicum.model.category.CategoryOut;
import ru.practicum.model.user.UserOutWithoutEmail;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class MessageShortOut {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;
    private String annotation;
    private CategoryOut category;
    private Long confirmedRequests;
    private Long id;
    private UserOutWithoutEmail initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
