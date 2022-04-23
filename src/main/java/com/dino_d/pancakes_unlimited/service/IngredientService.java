package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.IngredientDto;

public interface IngredientService {
    IngredientDto getIngredientById(long id);
    IngredientDto createIngredient(long id, IngredientDto ingredientDto);
}
