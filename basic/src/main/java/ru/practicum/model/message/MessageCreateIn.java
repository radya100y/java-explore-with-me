package ru.practicum.model.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@ToString
public class MessageCreateIn {

    @Size(max = 2000, min = 20, message = "поле annotation должно содержать между 20 и 2000 символов")
    @NotBlank
    private String annotation;

    @NotNull(message = "поле category должно быть заполнено")
    private Long category;

    @Size(max = 7000, min = 20, message = "поле description должно содержать между 20 и 7000 символов")
    @NotBlank
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

    @Size(max = 120, min = 3, message = "поле title должно содержать между 3 и 120 символов")
    private String title;

    private MessageStateAction stateAction;
}