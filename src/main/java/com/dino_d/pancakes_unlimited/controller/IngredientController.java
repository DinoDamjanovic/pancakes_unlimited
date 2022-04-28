package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.RequestIngredientDto;
import com.dino_d.pancakes_unlimited.dto.ResponseIngredientDto;
import com.dino_d.pancakes_unlimited.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/ingredients")
public class IngredientController {

    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<ResponseIngredientDto> createIngredient(
            @Valid @RequestBody RequestIngredientDto requestIngredientDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(requestIngredientDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseIngredientDto> getIngredientById(@PathVariable(name = "id") @Min(1) long id) {
        return new ResponseEntity<>(ingredientService.getIngredientById(id), HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ResponseIngredientDto>> getIngredientsByCategoryId(
            @PathVariable(name = "categoryId") @Min(1) long categoryId) {
        return new ResponseEntity<>(ingredientService.getIngredientsByCategoryId(categoryId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseIngredientDto> updateIngredientById(
            @Valid @RequestBody RequestIngredientDto requestIngredientDto,
            @PathVariable(name = "id") @Min(1) long id) {
        return new ResponseEntity<>(ingredientService.updateIngredientById(id, requestIngredientDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredientById(@PathVariable(name = "id") @Min(1) long id) {
        ingredientService.deleteIngredientById(id);
        return new ResponseEntity<>("Ingredient with id " + id + " deleted successfully.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponseIngredientDto>> getAllIngredients() {
        return new ResponseEntity<>(ingredientService.getAllIngredients(), HttpStatus.OK);
    }
}
