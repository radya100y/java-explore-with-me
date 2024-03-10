package ru.practicum.service.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.like.Like;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findAllByMessage_IdAndUser_Id(Long messageId, Long userId);
}
