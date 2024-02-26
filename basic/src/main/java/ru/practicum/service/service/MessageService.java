package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.model.message.MessageCreateIn;
import ru.practicum.model.message.MessageCreateOut;
import ru.practicum.model.message.MessageMapper;
import ru.practicum.model.user.User;

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

        return MessageMapper.toMessageCreateOut(messageRepository.save(MessageMapper.toMessage(message, category, user)));
    }
}
