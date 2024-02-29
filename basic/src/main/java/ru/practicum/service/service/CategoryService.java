package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.error.ConflictException;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryIn;
import ru.practicum.model.category.CategoryMapper;
import ru.practicum.model.category.CategoryOut;
import ru.practicum.error.NotFoundException;

import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Transactional
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryOut add(CategoryIn categoryIn) {
        try {
            return CategoryMapper.toCategoryOut(categoryRepository.save(CategoryMapper.toCategory(categoryIn)));
        } catch (DataIntegrityViolationException exc) {
            throw new ConflictException("Категория с именем " + categoryIn.getName() + " уже существует");
        }
    }

    public CategoryOut delete(long id) {
        CategoryOut category = getCategory(id);
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException exc) {
            throw new ConflictException("Категория не может быть удалена");
        }
        return category;
    }

    public CategoryOut update(long id, CategoryIn categoryIn) {
        getCategory(id);
        try {
            return CategoryMapper.toCategoryOut(categoryRepository.save(new Category(id, categoryIn.getName())));
        } catch (DataIntegrityViolationException exc) {
            throw new ConflictException("Категория с именем " + categoryIn.getName() + " уже существует");
        }
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public CategoryOut getCategory(long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toCategoryOut)
                .orElseThrow(() -> new NotFoundException("Категория с идентификатором " + id + " не найдена"));
    }

    @Transactional(value = Transactional.TxType.NOT_SUPPORTED)
    public List<CategoryOut> gets(Pageable reqPage) {
        return categoryRepository.getAllLimitNoQueringMethod(reqPage).stream()
                .map(CategoryMapper::toCategoryOut)
                .collect(Collectors.toList());
    }
}
