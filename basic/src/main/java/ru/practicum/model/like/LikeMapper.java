package ru.practicum.model.like;

import lombok.experimental.UtilityClass;
import ru.practicum.model.message.Message;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

@UtilityClass
public class LikeMapper {

    public static Like toLike(Message message, User user, Boolean rate) {
        return Like.builder()
                .message(message)
                .user(user)
                .rate(rate ? 1 : -1)
                .createdOn(LocalDateTime.now())
                .build();
    }
}
