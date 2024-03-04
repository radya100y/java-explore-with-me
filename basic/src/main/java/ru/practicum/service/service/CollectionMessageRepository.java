package ru.practicum.service.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.collection.CollectionMessage;

public interface CollectionMessageRepository extends JpaRepository<CollectionMessage, Long> {

    void deleteByCollection_Id(long collectionId);
}
