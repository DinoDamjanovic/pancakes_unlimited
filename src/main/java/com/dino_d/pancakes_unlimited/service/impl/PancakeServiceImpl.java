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

    @Override
    public ResponsePancakeDto getPancakeById(long id) {
        Pancake pancake = pancakeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pancake", "id", id));
        return mapToDto(pancake);
    }

    @Override
    public ResponsePancakeDto updatePancakeById(long id, RequestPancakeDto requestPancakeDto) {
        // check if pancake with provided id exists
        Pancake pancake = pancakeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pancake", "id", id));

        mapToEntity(requestPancakeDto, pancake);

        // delete old pancake ingredients entries
        pancakeIngredientsRepository.deleteByPancakeId(id);

        Pancake savedPancake = pancakeRepository.save(pancake);

        // TODO savedPancake has all Set entries == null for some reason. This is a workaround to fix the problem.
        mapToEntity(requestPancakeDto, savedPancake);

        // save new pancake ingredients entries
        Set<PancakeIngredients> pancakeIngredients = savedPancake.getPancakeIngredients();
        pancakeIngredientsRepository.saveAll(pancakeIngredients);

        return mapToDto(savedPancake);
    }

    @Override
    public void deletePancakeById(long id) {
        Pancake pancake = pancakeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pancake", "id", id));
        pancakeRepository.delete(pancake);

        // delete all entries in pancakes_with_ingredients table for this pancake
        pancakeIngredientsRepository.deleteByPancakeId(id);
    }

    @Override
    public List<ResponsePancakeDto> getAllPancakes() {
        List<Pancake> pancakes = pancakeRepository.findAll();
        return pancakes.stream().map(p -> mapToDto(p)).collect(Collectors.toList());
    }

    // convert entity to DTO
    private ResponsePancakeDto mapToDto(Pancake pancake) {
        ResponsePancakeDto responsePancakeDto = new ResponsePancakeDto();
        responsePancakeDto.setId(pancake.getId());

        List<String> ingredients = pancake.getPancakeIngredients().stream().map(
                p -> p.getIngredient().getName()).collect(Collectors.toList());
        responsePancakeDto.setIngredients(ingredients);

        return responsePancakeDto;
    }

    // convert DTO to entity
    private Pancake mapToEntity(RequestPancakeDto requestPancakeDto) {
        Pancake pancake = new Pancake();

        List<Ingredient> ingredients = getIngredients(requestPancakeDto);
        pancake.setPancakeIngredients(getPancakeIngredientsSet(pancake, ingredients));

        return pancake;
    }

    // update existing entity with DTO data
    private void mapToEntity(RequestPancakeDto requestPancakeDto, Pancake pancake) {
        List<Ingredient> ingredients = getIngredients(requestPancakeDto);
        pancake.setPancakeIngredients(getPancakeIngredientsSet(pancake, ingredients));
    }

    private List<Ingredient> getIngredients(RequestPancakeDto requestPancakeDto) {
        List<Long> ingredientIds = requestPancakeDto.getIngredientIds();
        List<Ingredient> ingredients = ingredientRepository.findAllById(ingredientIds);

        // if not all provided ingredients exist in db, raise an exception
        if (ingredientIds.size() != ingredients.size()) {
            List<Long> validIngredientIds = ingredients.stream().map(i -> i.getId()).collect(Collectors.toList());

            for (Long ingredientId : ingredientIds) {
                if (!validIngredientIds.contains(ingredientId)) {
                    throw new ResourceNotFoundException("Ingredient", "id", ingredientId);
                }
            }
        }

        return ingredients;
    }

    private Set<PancakeIngredients> getPancakeIngredientsSet(Pancake pancake, List<Ingredient> ingredients) {
        Set<PancakeIngredients> pancakeIngredients = new LinkedHashSet<>();
        for (Ingredient ingredient : ingredients) {
            pancakeIngredients.add(new PancakeIngredients(pancake, ingredient));
        }
        return pancakeIngredients;
    }
}
