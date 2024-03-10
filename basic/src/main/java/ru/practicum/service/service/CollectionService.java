package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.error.BadRequestException;
import ru.practicum.error.NotFoundException;
import ru.practicum.model.collection.*;
import ru.practicum.model.message.Message;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {

    @Autowired
    private final CollectionRepository collectionRepository;

    @Autowired
    private final CollectionMessageRepository collectionMessageRepository;

    @Autowired
    private final MessageRepository messageRepository;

    private Collection getCollection(Long collectionId) {
        return collectionRepository.findById(collectionId).orElseThrow(() ->
                new NotFoundException("Коллекция с ИД " + collectionId + " не найдена"));
    }

    @Transactional
    public CollectionCreateOut add(CollectionCreateIn collectionCreateIn) {

        //Нужно добавить проверку на уникальность в обход констраинтам
        Collection savedCollection = collectionRepository.save(CollectionMapper.toCollection(collectionCreateIn));
        Set<CollectionMessage> events = new HashSet<>();

        if (collectionCreateIn.getEvents() != null) {
            for (Long i : collectionCreateIn.getEvents()) {
                Message message = messageRepository.findById(i).orElseThrow(() ->
                        new NotFoundException("Событие " + i + " не найдено"));

                events.add(collectionMessageRepository.save(new CollectionMessage(message, savedCollection)));
            }
        }
        savedCollection.setEvents(events);

        return CollectionMapper.toCollectionCreateOut(savedCollection);
    }

    @Transactional
    public void delete(Long collectionId) {
        getCollection(collectionId);
        collectionRepository.deleteById(collectionId);
    }

    @Transactional
    public CollectionCreateOut update(Long collectionId, CollectionCreateIn collectionUpdateIn) {
        Collection savedCollection = getCollection(collectionId);

        if (collectionUpdateIn.getPinned() != null) savedCollection.setPinned(collectionUpdateIn.getPinned());
        if (collectionUpdateIn.getTitle() != null) {
            if (collectionUpdateIn.getTitle().length() > 50) throw new BadRequestException("Количество символов Ю 50");
            savedCollection.setTitle(collectionUpdateIn.getTitle());
        }
        if (collectionUpdateIn.getEvents() != null) {

            collectionMessageRepository.deleteByCollection_Id(collectionId);
            for (Long i : collectionUpdateIn.getEvents()) {
                Message message = messageRepository.findById(i).orElseThrow(() ->
                        new NotFoundException("Событие " + i + " не найдено"));

                collectionMessageRepository.save(new CollectionMessage(message, savedCollection));
            }
        }
        collectionRepository.save(savedCollection);

        return CollectionMapper.toCollectionCreateOut(getCollection(collectionId));
    }

    public CollectionCreateOut getCollectionOut(Long collectionId) {
        return CollectionMapper.toCollectionCreateOut(getCollection(collectionId));
    }

    public List<CollectionCreateOut> getCollections(Boolean pinned, Pageable pageable) {

        if (pinned == null) return collectionRepository.findAll(pageable).stream()
                .map(CollectionMapper::toCollectionCreateOut).collect(Collectors.toList());
        else return collectionRepository.findAllByPinned(pinned, pageable).stream()
                .map(CollectionMapper::toCollectionCreateOut).collect(Collectors.toList());
    }


}
