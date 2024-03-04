package ru.practicum.service.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Transactional
@Slf4j
public class MessageService {

    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RequestRepository requestRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MessageCreateOut add(MessageCreateIn message, long userId) {

        Category category = categoryRepository.findById(message.getCategory()).orElseThrow(() ->
                new NotFoundException("Категория с идентификатором " + message.getCategory() + " не найдена"));

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));

        if (message.getEventDate().isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS)))
            throw new ConflictException("Некорректное время события");

        Message savedMessage = MessageMapper.toMessage(message, category, user, new HashSet<>());
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

        if (message.getStateAction() != null) {

            if (message.getStateAction().equals(MessageStateAction.CANCEL_REVIEW)) {
                oldMessage.setState(MessageStatus.CANCELED);
            } else if (message.getStateAction().equals(MessageStateAction.SEND_TO_REVIEW)) {
                oldMessage.setState(MessageStatus.PENDING);
            }
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

        if (message.getStateAction() != null) {
            switch (message.getStateAction()) {
                case REJECT_EVENT: {
                    oldMessage.setState(MessageStatus.CANCELED);
                    break;
                }
                case PUBLISH_EVENT: {
                    oldMessage.setState(MessageStatus.PUBLISHED);
                    oldMessage.setPublishedOn(LocalDateTime.now());
                }
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
                                                    List<Long> categoryList, LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd, int from, int size) {

        QMessage message = QMessage.message;

        BooleanExpression whereUser = userList.size() == 0 ?
                Expressions.asBoolean(true).isTrue() :
                message.initiator.id.in(userList);

        BooleanExpression whereStatus = statusList.size() == 0 ?
                Expressions.asBoolean(true).isTrue() :
                message.state.in(statusList);

        BooleanExpression whereCategory = categoryList.size() == 0 ?
                Expressions.asBoolean(true).isTrue() :
                message.category.id.in(categoryList);

        BooleanExpression whereStart = rangeStart == null ?
                Expressions.asBoolean(true).isTrue() :
                message.eventDate.after(rangeStart);

        BooleanExpression whereEnd = rangeEnd == null ?
                Expressions.asBoolean(true).isTrue() :
                message.eventDate.before(rangeEnd);

        BooleanExpression whereCombo = whereUser.and(whereStatus).and(whereCategory).and(whereStart).and(whereEnd);

        List<Message> messages = new JPAQueryFactory(entityManager)
                .selectFrom(message)
                .from(message)
                .where(whereCombo)
                .limit(size)
                .offset(from)
                .fetch();


        return messages.stream()
                .map(MessageMapper::toMessageCreateOut)
                .collect(Collectors.toList());
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public List<MessageShortOut> findPublicMessages(String text, List<Long> categoryList, Boolean paid,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable, String sort, int from, int size)  {

        QMessage message = QMessage.message;

        BooleanExpression whereExpression = text == null ? Expressions.asBoolean(true).isTrue() :
                message.annotation.containsIgnoreCase(text).or(message.description.containsIgnoreCase(text));
        if (categoryList.size() > 0) whereExpression.and(message.category.id.in(categoryList));
        if (paid != null) whereExpression.and(message.paid.eq(paid));
        if (rangeStart == null) {
            whereExpression.and(message.eventDate.after(LocalDateTime.now()));
        } else {
            whereExpression.and(message.eventDate.after(rangeStart));
            if (rangeEnd != null) whereExpression.and(message.eventDate.before(rangeEnd));
        }
        if (onlyAvailable != null) {
            if (onlyAvailable) whereExpression.and(message.participantLimit.lt(message.requests.size())
                    .or(message.participantLimit.eq(0)));
        }

        List<Message> messages = new JPAQueryFactory(entityManager)
                .selectFrom(message)
                .from(message)
                .where(whereExpression)
                .orderBy(sort == null || sort.equals("EVENT_DATE") ? message.eventDate.desc() : message.eventDate.asc())
                .limit(size)
                .offset(from)
                .fetch();

        return messages.stream()
                .map(MessageMapper::toMessageShortOut)
                .collect(Collectors.toList());
    }

}
