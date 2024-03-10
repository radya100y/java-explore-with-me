package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.like.Like;
import ru.practicum.model.like.LikeMapper;
import ru.practicum.model.message.Message;

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

        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + "  не найден"));

        Message message = messageRepository.findById(messageId).orElseThrow(() ->
                new NotFoundException("Событие с идентификатором " + messageId + " не найдено"));

        Long rateIncr = rate ? 1L : -1L;

        Like savedOrNewLike = likeRepository.findFirstByMessageIdAndUserId(messageId, userId)
                .orElse(LikeMapper.toLike(messageId, userId, rateIncr));
        savedOrNewLike.setRate(rateIncr);

        Like savedLike = likeRepository.save(savedOrNewLike);

        Long newRating = likeRepository.findAllByMessageId(messageId).stream()
                .map(Like::getRate)
                .mapToLong(x -> x)
                .sum();
        message.setRating(newRating);
        messageRepository.save(message);

        return savedLike;
    }
}
