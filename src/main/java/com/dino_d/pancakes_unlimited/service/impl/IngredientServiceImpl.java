package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.IngredientDto;
import com.dino_d.pancakes_unlimited.entity.Category;
import com.dino_d.pancakes_unlimited.entity.Ingredient;
import com.dino_d.pancakes_unlimited.exception.ResourceNotFoundException;
import com.dino_d.pancakes_unlimited.repository.CategoryRepository;
import com.dino_d.pancakes_unlimited.repository.IngredientRepository;
import com.dino_d.pancakes_unlimited.service.IngredientService;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService {

    private IngredientRepository ingredientRepository;
    private CategoryRepository categoryRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, CategoryRepository categoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public IngredientDto createIngredient(long id, IngredientDto ingredientDto) {
        Ingredient ingredient = mapToEntity(ingredientDto);

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id));
        ingredient.setCategory(category);
        ingredientRepository.save(ingredient);

        return mapToDto(ingredient);
    }

    @Override
    public IngredientDto getIngredientById(long id) {
        Ingredient category = ingredientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id));
        return mapToDto(category);
    }

    // convert entity to DTO
    private IngredientDto mapToDto(Ingredient ingredient) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setPrice(ingredient.getPrice());
        ingredientDto.setCategoryId(ingredient.getCategory().getId());

        return ingredientDto;
    }

    // convert DTO to entity
    private Ingredient mapToEntity(IngredientDto ingredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDto.getName());
        ingredient.setPrice(ingredientDto.getPrice());

        return ingredient;
    }
}
