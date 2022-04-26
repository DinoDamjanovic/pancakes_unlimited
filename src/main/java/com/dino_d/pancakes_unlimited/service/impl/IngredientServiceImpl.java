package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.RequestIngredientDto;
import com.dino_d.pancakes_unlimited.dto.ResponseIngredientDto;
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
    public ResponseIngredientDto createIngredient(RequestIngredientDto requestIngredientDto) {
        Ingredient ingredient = mapToEntity(requestIngredientDto);

        long categoryId = requestIngredientDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId));
        ingredient.setCategory(category);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return mapToDto(savedIngredient);
    }

    @Override
    public ResponseIngredientDto getIngredientById(long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient", "id", id));
        return mapToDto(ingredient);
    }

    @Override
    public List<ResponseIngredientDto> getIngredientsByCategoryId(long categoryId) {
        List<Ingredient> ingredients = ingredientRepository.findByCategoryId(categoryId);
        return ingredients.stream().map(ingredient -> mapToDto(ingredient)).collect(Collectors.toList());
    }

    @Override
    public ResponseIngredientDto updateIngredientById(long id, RequestIngredientDto requestIngredientDto) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient", "id", id));
        ingredient.setName(requestIngredientDto.getName());
        ingredient.setPrice(requestIngredientDto.getPrice());

        long categoryId = requestIngredientDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId));
        ingredient.setCategory(category);
        Ingredient updatedIngredient = ingredientRepository.save(ingredient);

        return mapToDto(updatedIngredient);
    }

    @Override
    public void deleteIngredientById(long id) {
        Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient", "id", id));
        ingredientRepository.delete(ingredient);
    }

    @Override
    public List<ResponseIngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream().map(ingredient -> mapToDto(ingredient)).collect(Collectors.toList());
    }

    // convert entity to DTO
    private ResponseIngredientDto mapToDto(Ingredient ingredient) {
        ResponseIngredientDto responseIngredientDto = new ResponseIngredientDto();
        responseIngredientDto.setId(ingredient.getId());
        responseIngredientDto.setName(ingredient.getName());
        responseIngredientDto.setPrice(ingredient.getPrice());
        responseIngredientDto.setCategory(ingredient.getCategory().getName());

        return responseIngredientDto;
    }

    // convert DTO to entity
    private Ingredient mapToEntity(RequestIngredientDto requestIngredientDto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(requestIngredientDto.getName());
        ingredient.setPrice(requestIngredientDto.getPrice());

        return ingredient;
    }
}
