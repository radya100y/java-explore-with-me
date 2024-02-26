package ru.practicum.model.message;

import lombok.experimental.UtilityClass;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryMapper;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserMapper;

import java.time.LocalDateTime;

@UtilityClass
public class MessageMapper {

    public static Message toMessageCreate(MessageCreateIn messageIn, Category category, User user) {
        return Message.builder()
                .annotation(messageIn.getAnnotation())
                .category(category)
                .description(messageIn.getDescription())
                .eventDate(messageIn.getEventDate())
                .lon(messageIn.getLocation().getLon())
                .lat(messageIn.getLocation().getLat())
                .paid(messageIn.getPaid())
                .participantLimit(messageIn.getParticipantLimit())
                .requestModeration(messageIn.getRequestModeration())
                .title(messageIn.getTitle())
                .initiator(user)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static MessageCreateOut toMessageCreateOut(Message message) {
        return MessageCreateOut.builder()
                .annotation(message.getAnnotation())
                .category(CategoryMapper.toCategoryOut(message.getCategory()))
                .createdOn(message.getCreatedOn())
                .description(message.getDescription())
                .eventDate(message.getEventDate())
                .id(message.getId())
                .initiator(UserMapper.toUserOutWithoutEmail(message.getInitiator()))
                .location(new Location(message.getLat(), message.getLon()))
                .paid(message.getPaid())
                .participantLimit(message.getParticipantLimit())
                .publishedOn(message.getPublishedOn())
                .requestModeration(message.getRequestModeration())
                .state(message.getState())
                .title(message.getTitle())
                .views(1000)
                .confirmedRequests(1000)
                .build();
    }

    public static Message toMessageUpdate(MessageUpdateIn messageIn, Category category, User user) {
        return Message.builder()
                .annotation(messageIn.getAnnotation())
                .category(category)
                .description(messageIn.getDescription())
                .eventDate(messageIn.getEventDate())
                .lon(messageIn.getLocation().getLon())
                .lat(messageIn.getLocation().getLat())
                .paid(messageIn.getPaid())
                .participantLimit(messageIn.getParticipantLimit())
                .requestModeration(messageIn.getRequestModeration())
                .title(messageIn.getTitle())
                .initiator(user)
                .createdOn(LocalDateTime.now())
                .build();
    }
}

