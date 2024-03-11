package ru.practicum.model.like;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class LikeMapper {

    public static Like toLike(Long messageId, Long userId, Long rate, Long initiatorId) {
        return Like.builder()
                .messageId(messageId)
                .userId(userId)
                .rate(rate)
                .createdOn(LocalDateTime.now())
                .initiatorId(initiatorId)
                .build();
    }
}
