package ru.practicum.ewm.service;

import ru.practicum.ewm.entity.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);

    List<Category> getCategories(int from, int size);

    Category getCategoryById(long id);

    void deleteCategoryById(long id);

    Category patchCategoryById(Category category, long id);
}
