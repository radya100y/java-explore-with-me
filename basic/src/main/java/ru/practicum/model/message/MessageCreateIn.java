package ru.practicum.model.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Getter
public class MessageCreateIn {

    @Size(max = 1024, message = "поле annotation должно содержать не более 1024 символов")
    private String annotation;

    @NotNull(message = "поле category должно быть заполнено")
    private long category;

    @Size(max = 1024, message = "поле description должно содержать не более 1024 символов")
    private String description;

    @Future(message = "поле eventDate должно быть в будующем")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private Location location;

    @NotNull(message = "поле paid должно быть заполнено")
    private Boolean paid;

    @NotNull(message = "поле participantLimit должно быть заполнено")
    @Positive(message = "поле participantLimit должно быть больше 0")
    private Integer participantLimit;

    @NotNull(message = "поле requestModeration должно быть заполнено")
    private Boolean requestModeration;

    @Size(max = 512, message = "поле title должно содержать не более 512 символов")
    private String title;
}
