package ru.practicum.service.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.message.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
