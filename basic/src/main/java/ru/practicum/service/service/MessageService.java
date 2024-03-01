package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.model.message.*;
import ru.practicum.model.message.QMessage;
import ru.practicum.model.request.RequestMapper;
import ru.practicum.model.request.RequestOut;
import ru.practicum.model.user.User;

import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RequestRepository requestRepository;

    public MessageCreateOut add(MessageCreateIn message, long userId) {

        Category category = categoryRepository.findById(message.getCategory()).orElseThrow(() ->
                new NotFoundException("Категория с идентификатором " + message.getCategory() + " не найдена"));

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));

        if (message.getEventDate().isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS)))
            throw new ConflictException("Некорректное время события");

        Message savedMessage = MessageMapper.toMessage(message, category, user, new ArrayList<>());
        savedMessage.setState(MessageStatus.PENDING);

        return MessageMapper.toMessageCreateOut(messageRepository.save(savedMessage));
    }

    public MessageCreateOut update(MessageUpdateIn message, long userId, long messageId) {

        Message oldMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Событие с идентификатором " + messageId + " не найдено"));

        if (oldMessage.getState().equals(MessageStatus.PUBLISHED))
            throw new ConflictException("Редактирование этих событий запрещено");

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

        if (message.getStateAction().equals(MessageStateAction.CANCEL_REVIEW)) {
            oldMessage.setState(MessageStatus.CANCELED);
        } else if (message.getStateAction().equals(MessageStateAction.SEND_TO_REVIEW)) {
            oldMessage.setState(MessageStatus.PENDING);
        }

        return MessageMapper.toMessageCreateOut(messageRepository.save(oldMessage));
    }

    public MessageCreateOut updateAdmin(MessageUpdateIn message, long messageId) {

        Message oldMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Событие с идентификатором " + messageId + " не найдено"));

        if (!oldMessage.getState().equals(MessageStatus.PENDING))
            throw new ConflictException("Редактирование этих событий запрещено");

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
            case REJECT_EVENT: {
                oldMessage.setState(MessageStatus.CANCELED);
                break;
            } case PUBLISH_EVENT: {
                oldMessage.setState(MessageStatus.PUBLISHED);
                oldMessage.setPublishedOn(LocalDateTime.now());
            }
        }

        return MessageMapper.toMessageCreateOut(messageRepository.save(oldMessage));
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public List<MessageShortOut> getForInitiator(long userId, Pageable pageable) {

        return messageRepository.findAllByInitiator_Id(userId, pageable).stream()
                .map(MessageMapper::toMessageShortOut)
                .collect(Collectors.toList());
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public MessageCreateOut getMessage(long userId, long messageId) {

        return MessageMapper.toMessageCreateOut(messageRepository.findById(messageId).orElseThrow(() ->
                new NotFoundException("Событие с идентификатором " + messageId + " не найдено")));
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public List<RequestOut> getRequestsForMessage(long userId, long messageId) {

        return requestRepository.findAllByEvent_Id(messageId).stream()
                .map(RequestMapper::toRequestOut)
                .collect(Collectors.toList());
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public MessageCreateOut getMessageForPublic(long messageId) {

        MessageCreateOut returmedMessage = MessageMapper.toMessageCreateOut(
                messageRepository.findById(messageId)
                        .orElseThrow(() ->
                                new NotFoundException("Событие с идентификатором " + messageId + " не найдено")));

        if (!returmedMessage.getState().equals(MessageStatus.PUBLISHED))
            throw new NotFoundException("Событие " + messageId + " не найдено");
        return returmedMessage;
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public List<MessageCreateOut> findAdminMessages(List<Long> userList, List<MessageStatus> statusList,
                                                    List<String> categoryList, LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd, Pageable pageable) {

        QMessage message = QMessage.message;

        return null;
    }

}
