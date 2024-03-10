package ru.practicum.model.like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeIn {

    @NotNull
    private Long messageId;

    @NotNull
    private Long userId;

    @NotNull
    private Boolean rate;
}
