package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.collection.*;
import ru.practicum.model.message.Message;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollectionService {

    @Autowired
    private final CollectionRepository collectionRepository;

    @Autowired
    private final CollectionMessageRepository collectionMessageRepository;

    @Autowired
    private final MessageRepository messageRepository;

    @Transactional
    public CollectionCreateOut add(CollectionCreateIn collectionCreateIn) {

        Collection savedCollection = collectionRepository.save(CollectionMapper.toCollection(collectionCreateIn));
        Set<CollectionMessage> events = new HashSet<>();

        for (Long i : collectionCreateIn.getEvents()) {
            Message message = messageRepository.findById(i).orElseThrow(() ->
                    new NotFoundException("Событие " + i + " не найдено"));

            events.add(collectionMessageRepository.save(new CollectionMessage(message, savedCollection)));
        }
        savedCollection.setEvents(events);

        return CollectionMapper.toCollectionCreateOut(savedCollection);
    }
}
