package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.RequestPancakeDto;
import com.dino_d.pancakes_unlimited.dto.ResponsePancakeDto;
import com.dino_d.pancakes_unlimited.entity.Ingredient;
import com.dino_d.pancakes_unlimited.entity.Pancake;
import com.dino_d.pancakes_unlimited.entity.PancakeIngredients;
import com.dino_d.pancakes_unlimited.exception.ResourceNotFoundException;
import com.dino_d.pancakes_unlimited.repository.IngredientRepository;
import com.dino_d.pancakes_unlimited.repository.PancakeRepository;
import com.dino_d.pancakes_unlimited.repository.PancakeIngredientsRepository;
import com.dino_d.pancakes_unlimited.service.PancakeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PancakeServiceImpl implements PancakeService {

    private PancakeRepository pancakeRepository;
    private PancakeIngredientsRepository pancakeIngredientsRepository;
    private IngredientRepository ingredientRepository;

    public PancakeServiceImpl(PancakeRepository pancakeRepository,
                              PancakeIngredientsRepository pancakeIngredientsRepository,
                              IngredientRepository ingredientRepository) {
        this.pancakeRepository = pancakeRepository;
        this.pancakeIngredientsRepository = pancakeIngredientsRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public ResponsePancakeDto createPancake(RequestPancakeDto requestPancakeDto) {
        Pancake pancake = mapToEntity(requestPancakeDto);

        Pancake savedPancake = pancakeRepository.save(pancake);

        Set<PancakeIngredients> pancakeIngredients = savedPancake.getPancakeIngredients();
        pancakeIngredientsRepository.saveAll(pancakeIngredients);

        return mapToDto(savedPancake);
    }

    // convert entity to DTO
    private ResponsePancakeDto mapToDto(Pancake pancake) {
        ResponsePancakeDto responsePancakeDto = new ResponsePancakeDto();
        responsePancakeDto.setId(pancake.getId());

        List<String> ingredients = pancake.getPancakeIngredients().stream().map(
                p -> p.getIngredient().getName()).collect(Collectors.toList());
        responsePancakeDto.setIngredientsNames(ingredients);

        return responsePancakeDto;
    }

    // convert DTO to entity
    private Pancake mapToEntity(RequestPancakeDto requestPancakeDto) {
        Pancake pancake = new Pancake();

        List<Long> ingredientsIds = requestPancakeDto.getIngredientsIds();
        List<Ingredient> ingredients = new ArrayList<>();
        for (Long ingredientId : ingredientsIds) {
            Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(
                    () -> new ResourceNotFoundException("Ingredient", "id", ingredientId));
            ingredients.add(ingredient);
        }

        Set<PancakeIngredients> pancakeIngredients = new LinkedHashSet<>();
        for (Ingredient ingredient : ingredients) {
            pancakeIngredients.add(new PancakeIngredients(pancake, ingredient));
        }
        pancake.setPancakeIngredients(pancakeIngredients);

        return pancake;
    }
}
