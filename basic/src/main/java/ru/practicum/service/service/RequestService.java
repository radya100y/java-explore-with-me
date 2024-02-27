package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.error.AlreadyExistException;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.message.Message;
import ru.practicum.model.message.MessageStatus;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestMapper;
import ru.practicum.model.request.RequestOut;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final MessageRepository messageRepository;

    public RequestOut addRequest(long userId, long eventId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));

        Message message = messageRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с идентификатором " + eventId + " не найдено"));

        List<Request> requests = requestRepository.findAllByEvent_IdAndStatusIn(
                eventId,
                List.of(RequestStatus.APPROVED, RequestStatus.PENDING)
        );

        if (message.getInitiator().getId() == userId)
            throw new ConflictException("Нельзя подать несколько заявок");
        if (!message.getState().equals(MessageStatus.PUBLISHED))
            throw new ConflictException("Нельзя оставить заявку на неопубликованное событие");
        if (requests.size() >= message.getParticipantLimit())
            throw new ConflictException("Лимит участников события превышен");

        Request request = Request.builder()
                .event(message)
                .requester(user)
                .createdOn(LocalDateTime.now())
                .status(message.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.APPROVED)
                .build();
        try {
            return RequestMapper.toRequestOut(requestRepository.save(request));
        } catch (DataIntegrityViolationException exc) {
            throw new AlreadyExistException("Нельзя создать несколько заявок на участие в событии");
        }

    }

    public RequestOut cancelRequest(long userId, long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));

        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с идентификатором " + requestId + " не найден"));

        if (request.getRequester().getId() == userId) request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestOut(requestRepository.save(request));
    }
}
