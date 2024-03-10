package ru.practicum.service.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.like.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

}
