package ru.practicum.service.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.like.Like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findFirstByMessageIdAndUserId(Long messageId, Long userId);

    List<Like> findAllByMessageId(Long messageId);
}
