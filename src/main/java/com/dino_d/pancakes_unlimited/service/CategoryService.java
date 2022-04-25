package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto getCategoryById(long id);

    CategoryDto getCategoryByName(String name);

    CategoryDto updateCategoryById(long id, CategoryDto categoryDto);

    void deleteCategoryById(long id);

    List<CategoryDto> getAllCategories();
}
