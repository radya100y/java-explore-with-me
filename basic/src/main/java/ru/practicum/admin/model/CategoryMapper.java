package ru.practicum.admin.model;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryMapper {

    public static Category toCategory(CategoryIn categoryIn) {
        return new Category(categoryIn.getName());
    }

    public static CategoryOut toCategoryOut(Category category) {
        return new CategoryOut(
                category.getId(),
                category.getName()
        );
    }
}
