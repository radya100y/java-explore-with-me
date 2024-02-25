package ru.practicum.admin.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.admin.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
