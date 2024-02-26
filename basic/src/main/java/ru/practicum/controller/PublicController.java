package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.CategoryOut;
import ru.practicum.service.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {

    @Autowired
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryOut> gets(@RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {

        Pageable reqPage = PageRequest.of(from / size, size, Sort.by("id").ascending());
        return categoryService.gets(reqPage);
    }

    @GetMapping("categories/{id}")
    public CategoryOut get(@PathVariable("id") long catId) {
        return categoryService.getCategory(catId);
    }
}
