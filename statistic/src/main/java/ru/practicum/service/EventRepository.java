package ru.practicum.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Event;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
}
