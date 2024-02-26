package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryIn;
import ru.practicum.model.category.CategoryMapper;
import ru.practicum.model.category.CategoryOut;
import ru.practicum.error.AlreadyExistException;
import ru.practicum.error.NotFoundException;

import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryOut add(CategoryIn categoryIn) {
        try {
            return CategoryMapper.toCategoryOut(categoryRepository.save(CategoryMapper.toCategory(categoryIn)));
        } catch (DataIntegrityViolationException exc) {
            throw new AlreadyExistException("Категория с именем " + categoryIn.getName() + " уже существует");
        }
    }

    @Transactional
    public CategoryOut delete(long id) {
        CategoryOut category = getCategory(id);
        categoryRepository.deleteById(id);
        return category;
    }

    @Transactional
    public CategoryOut update(long id, CategoryIn categoryIn) {
        getCategory(id);
        return CategoryMapper.toCategoryOut(categoryRepository.save(new Category(id, categoryIn.getName())));
    }

    public CategoryOut getCategory(long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toCategoryOut)
                .orElseThrow(() -> new NotFoundException("Категория с идентификатором " + id + " не найдена"));
    }

    public List<CategoryOut> gets(Pageable reqPage) {
        return categoryRepository.getAllLimitNoQueringMethod(reqPage).stream()
                .map(CategoryMapper::toCategoryOut)
                .collect(Collectors.toList());
    }
}
