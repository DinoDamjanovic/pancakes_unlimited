package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.RequestIngredientDto;
import com.dino_d.pancakes_unlimited.dto.ResponseIngredientDto;

import java.util.List;

public interface IngredientService {
    ResponseIngredientDto createIngredient(RequestIngredientDto requestIngredientDto);

    ResponseIngredientDto getIngredientById(long id);

    List<ResponseIngredientDto> getIngredientsByCategoryId(long categoryId);

    List<ResponseIngredientDto> getAllIngredients();

    ResponseIngredientDto updateIngredientById(long id, RequestIngredientDto requestIngredientDto);

    void deleteIngredientById(long id);
}
