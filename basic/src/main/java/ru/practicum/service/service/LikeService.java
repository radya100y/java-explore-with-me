package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.error.ConflictException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.like.Like;
import ru.practicum.model.like.LikeMapper;
import ru.practicum.model.message.Message;
import ru.practicum.model.request.RequestStatus;
import ru.practicum.model.user.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    @Autowired
    private final LikeRepository likeRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final RequestRepository requestRepository;

    @Transactional
    public Like addLike(long messageId, long userId, boolean rate) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + "  не найден"));

        Message message = messageRepository.findById(messageId).orElseThrow(() ->
                new NotFoundException("Событие с идентификатором " + messageId + " не найдено"));

        Long rateIncr = rate ? 1L : -1L;

        Like savedOrNewLike = likeRepository.findFirstByMessageIdAndUserId(messageId, userId)
                .orElse(LikeMapper.toLike(messageId, userId, rateIncr, message.getInitiator().getId()));
        savedOrNewLike.setRate(rateIncr);

        //Проверяем что текущая дата менее даты начала события, ИЛИ у пользователя есть одобренный реквест
        if (message.getEventDate().isAfter(LocalDateTime.now()) ||
                requestRepository.findAllByEvent_IdAndStatusIn(
                        messageId, List.of(RequestStatus.CONFIRMED, RequestStatus.APPROVED)).isEmpty()) {
            throw new ConflictException("Пользователь " + userId + " не может изменить рейтинг события " + messageId);
        }

        Like savedLike = likeRepository.save(savedOrNewLike);

        // Изменяем рейтинг у события
        Long messageRating = likeRepository.findAllByMessageId(messageId).stream()
                .map(Like::getRate)
                .mapToLong(x -> x)
                .sum();
        message.setRating(messageRating);
        messageRepository.save(message);

        //Изменяем рейтинг у инициатора
        User initiator = message.getInitiator();
        Long userRating = likeRepository.findAllByInitiatorId(initiator.getId()).stream()
                .map(Like::getRate)
                .mapToLong(x -> x)
                .sum();

        initiator.setRating(userRating);
        userRepository.save(initiator);

        return savedLike;
    }
}
