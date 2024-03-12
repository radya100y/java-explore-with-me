package ru.practicum.model.like;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
public class LikeId implements Serializable {

    private Long messageId;

    private Long userId;

    public LikeId(Long messageId, Long userId) {
        this.messageId = messageId;
        this.userId = userId;
    }
}
