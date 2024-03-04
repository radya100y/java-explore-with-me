package ru.practicum.model.message;

import lombok.experimental.UtilityClass;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryMapper;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserMapper;

import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
public class MessageMapper {

    public static Message toMessage(MessageCreateIn messageIn, Category category, User user, Set<Request> requests) {
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
                .requests(requests)
                .views(0L)
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
                .views(message.getViews())
                .confirmedRequests(message.getConfirmedRequestQty())
                .build();
    }

    public static MessageShortOut toMessageShortOut(Message message) {
        return MessageShortOut.builder()
                .eventDate(message.getEventDate())
                .annotation(message.getAnnotation())
                .category(CategoryMapper.toCategoryOut(message.getCategory()))
                .confirmedRequests(message.getConfirmedRequestQty())
                .id(message.getId())
                .initiator(UserMapper.toUserOutWithoutEmail(message.getInitiator()))
                .paid(message.getPaid())
                .title(message.getTitle())
                .views(message.getViews()).build();
    }
}

