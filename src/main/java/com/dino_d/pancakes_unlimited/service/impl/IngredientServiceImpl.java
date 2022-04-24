package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.IngredientDto;
import com.dino_d.pancakes_unlimited.entity.Category;
import com.dino_d.pancakes_unlimited.entity.Ingredient;
import com.dino_d.pancakes_unlimited.exception.ResourceNotFoundException;
import com.dino_d.pancakes_unlimited.repository.CategoryRepository;
import com.dino_d.pancakes_unlimited.repository.IngredientRepository;
import com.dino_d.pancakes_unlimited.service.IngredientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    private IngredientRepository ingredientRepository;
    private CategoryRepository categoryRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, CategoryRepository categoryRepository) {
        this.ingredientRepository = ingredientRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public IngredientDto createIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = mapToEntity(ingredientDto);

        long categoryId = ingredientDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId));
        ingredient.setCategory(category);
        ingredientRepository.save(ingredient);

        return mapToDto(ingredient);
    }

    @Override
    public IngredientDto getIngredientById(long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id));
        return mapToDto(ingredient);
    }

    @Override
    public List<IngredientDto> getIngredientsByCategoryId(long categoryId) {
        List<Ingredient> ingredients = ingredientRepository.findByCategoryId(categoryId);
        return ingredients.stream().map(ingredient -> mapToDto(ingredient)).collect(Collectors.toList());
    }

    @Override
    public IngredientDto updateIngredientById(long id, IngredientDto ingredientDto) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient", "id", id));
        ingredient.setName(ingredientDto.getName());
        ingredient.setPrice(ingredientDto.getPrice());

        long categoryId = ingredientDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId));
        ingredient.setCategory(category);
        ingredientRepository.save(ingredient);

        return mapToDto(ingredient);
    }

    @Override
    public void deleteIngredientById(long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient", "id", id));
        ingredientRepository.delete(ingredient);
    }

    @Override
    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream().map(ingredient -> mapToDto(ingredient)).collect(Collectors.toList());
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
