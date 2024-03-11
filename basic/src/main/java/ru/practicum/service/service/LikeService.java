package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.like.Like;
import ru.practicum.model.like.LikeMapper;
import ru.practicum.model.message.Message;
import ru.practicum.model.user.User;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    @Autowired
    private final LikeRepository likeRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final MessageRepository messageRepository;

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

        Like savedLike = likeRepository.save(savedOrNewLike);

        // Изменяем рейтинг у события
        Long messageRating = likeRepository.findAllByMessageId(messageId).stream()
                .map(Like::getRate)
                .mapToLong(x -> x)
                .sum();
        message.setRating(messageRating);
        messageRepository.save(message);

        //Изменяем рейтинг у создателя мероприятия
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
