package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.CategoryDto;
import com.dino_d.pancakes_unlimited.entity.Category;
import com.dino_d.pancakes_unlimited.repository.CategoryRepository;
import com.dino_d.pancakes_unlimited.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        // convert DTO to entity
        Category category = new Category();
        category.setName(categoryDto.getName());

        Category savedCategory = categoryRepository.save(category);

        // convert saved entity to DTO
        CategoryDto categoryDtoResponse = new CategoryDto();
        categoryDtoResponse.setId(savedCategory.getId());
        categoryDtoResponse.setName(savedCategory.getName());

        return categoryDtoResponse;
    }
}
