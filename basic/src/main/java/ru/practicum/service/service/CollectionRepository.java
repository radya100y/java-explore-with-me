package ru.practicum.service.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.collection.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

}
