package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.IngredientDto;

import java.util.List;

public interface IngredientService {
    IngredientDto createIngredient(IngredientDto ingredientDto);

    IngredientDto getIngredientById(long id);

    List<IngredientDto> getIngredientsByCategoryId(long categoryId);

    List<IngredientDto> getAllIngredients();

    IngredientDto updateIngredientById(long id, IngredientDto ingredientDto);

    void deleteIngredientById(long id);
}
