package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.message.Message;
import ru.practicum.model.message.MessageStatus;
import ru.practicum.model.request.*;
import ru.practicum.model.user.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final MessageRepository messageRepository;

    @Transactional
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
            throw new ConflictException("Нельзя создать несколько заявок на участие в событии");
        }

    }

    @Transactional
    public RequestOut cancelRequest(long userId, long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));

        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с идентификатором " + requestId + " не найден"));

        if (request.getRequester().getId() == userId) request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestOut(requestRepository.save(request));
    }

    @Transactional
    public RequestConfirmOut updateRequests(long userId, long eventId, RequestConfirmIn requests) {

        RequestConfirmOut rco = new RequestConfirmOut();

        Message message = messageRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с идентификатором " + eventId + " не найдено"));
        if (message.getInitiator().getId() != userId) throw
                new ConflictException("Пользователь " + userId + " не является инициатором события " + eventId);

        for (Long requestId : requests.getRequestIds()) {
            Request request = requestRepository.findById(requestId).orElseThrow(() ->
                    new NotFoundException("Запрос с идентификатором " + requestId + " не найден"));
            if (request.getEvent().getId() != eventId)
                throw new NotFoundException("Запрос " + requestId + " не принадлежит событию " + eventId);
            if (!request.getStatus().equals(RequestStatus.PENDING))
                throw new ConflictException("Заявка " + request.getId() + " уже обработана");

            int i = requestRepository.findAllByEvent_IdAndStatusIn(eventId, List.of(RequestStatus.APPROVED)).size();
            if (requests.getStatus().equals(RequestStatus.CONFIRMED) &&
                    (i < message.getParticipantLimit() || message.getParticipantLimit() == 0)) {
                request.setStatus(RequestStatus.CONFIRMED);
                requestRepository.save(request);

                rco.getConfirmedRequests().add(RequestMapper.toRequestOut(request));
            } else {
                request.setStatus(RequestStatus.REJECTED);
                requestRepository.save(request);
                rco.getRejectedRequests().add(RequestMapper.toRequestOut(request));
            }
        }
        return rco;
    }
}
