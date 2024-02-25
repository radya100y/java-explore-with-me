package ru.practicum.admin.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.admin.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
