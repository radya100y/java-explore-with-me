package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.model.message.*;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final UserRepository userRepository;

    public MessageCreateOut add(MessageCreateIn message, long userId) {

        Category category = categoryRepository.findById(message.getCategory()).orElseThrow(() ->
                new NotFoundException("Категория с идентификатором " + message.getCategory() + " не найдена"));

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));

        if (message.getEventDate().isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS)))
            throw new ConflictException("Некорректное время события");

        Message savedMessage = MessageMapper.toMessage(message, category, user);
        savedMessage.setState(MessageStatus.PENDING);

        return MessageMapper.toMessageCreateOut(messageRepository.save(savedMessage));
    }

    public MessageCreateOut update(MessageUpdateIn message, long userId, long messageId) {

        Message oldMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Событие с идентификатором " + messageId + " не найдено"));

        if (oldMessage.getState().equals(MessageStatus.PUBLISHED))
            throw new ConflictException("Редактирование опубликованных событий запрещено");

        if (message.getAnnotation() != null) oldMessage.setAnnotation(message.getAnnotation());
        if (message.getDescription() != null) oldMessage.setDescription(message.getDescription());
        if (message.getEventDate() != null) {
            if (message.getEventDate().isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
                throw new ConflictException("Некорректное время события");
            } else oldMessage.setEventDate(message.getEventDate());
        }
        if (message.getPaid() != null) oldMessage.setPaid(message.getPaid());
        if (message.getParticipantLimit() != null) oldMessage.setParticipantLimit(message.getParticipantLimit());
        if (message.getRequestModeration() != null) oldMessage.setRequestModeration(message.getRequestModeration());
        if (message.getTitle() != null) oldMessage.setTitle(message.getTitle());

        if (message.getStateAction().equals(MessageStateAction.CANCEL_REVIEW))
            oldMessage.setState(MessageStatus.CANCELED);

        return MessageMapper.toMessageCreateOut(messageRepository.save(oldMessage));
    }

    public MessageCreateOut updateAdmin(MessageUpdateIn message, long messageId) {

        Message oldMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Событие с идентификатором " + messageId + " не найдено"));

        if (message.getAnnotation() != null) oldMessage.setAnnotation(message.getAnnotation());
        if (message.getDescription() != null) oldMessage.setDescription(message.getDescription());
        if (message.getEventDate() != null) {
            if (message.getEventDate().isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
                throw new ConflictException("Некорректное время события");
            } else oldMessage.setEventDate(message.getEventDate());
        }
        if (message.getPaid() != null) oldMessage.setPaid(message.getPaid());
        if (message.getParticipantLimit() != null) oldMessage.setParticipantLimit(message.getParticipantLimit());
        if (message.getRequestModeration() != null) oldMessage.setRequestModeration(message.getRequestModeration());
        if (message.getTitle() != null) oldMessage.setTitle(message.getTitle());

        switch (message.getStateAction()) {
            case CANCEL_REVIEW: {
                oldMessage.setState(MessageStatus.CANCELED);
                break;
            } case PUBLISH_EVENT: {
                oldMessage.setState(MessageStatus.PUBLISHED);
                oldMessage.setPublishedOn(LocalDateTime.now());
            }
        }

        return MessageMapper.toMessageCreateOut(messageRepository.save(oldMessage));
    }
}
