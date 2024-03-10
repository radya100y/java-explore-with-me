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

    @Autowired
    private final MessageService messageService;

    @Transactional
    public Like addLike(long messageId, long userId, boolean rate) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + "  не найден"));

        Message message = messageRepository.findById(messageId).orElseThrow(() ->
                new NotFoundException("Событие с идентификатором " + messageId + " не найдено"));

        Like like = likeRepository.findAllByMessage_IdAndUser_Id(messageId, userId).get(0);

        long ratingIncr = rate ? 1 : -1;
        long ratingCur = message.getRating();

        if (like != null) {
            ratingCur = like.getRate() == -1 ? 1 : -1;
        }
        message.setRating(ratingCur + ratingIncr);
        messageRepository.save(message);

        return likeRepository.save(LikeMapper.toLike(message, user, rate));
    }
}
