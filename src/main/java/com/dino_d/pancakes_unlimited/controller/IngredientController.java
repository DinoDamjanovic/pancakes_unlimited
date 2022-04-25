package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.IngredientDto;
import com.dino_d.pancakes_unlimited.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody IngredientDto ingredientDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(ingredientDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredientById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(ingredientService.getIngredientById(id), HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<IngredientDto>> getIngredientsByCategoryId(@PathVariable(name = "categoryId") long categoryId) {
        return new ResponseEntity<>(ingredientService.getIngredientsByCategoryId(categoryId), HttpStatus.OK);
    }

    //TODO Maybe this is a better solution than getIngredientsByCategoryId() method above?
//    @GetMapping
//    public List<IngredientDto> getIngredientsByCategoryIdQuery(@RequestParam(name = "categoryId") long categoryId) {
//        return ingredientService.getIngredientsByCategoryId(categoryId);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> updateIngredientById(@RequestBody IngredientDto ingredientDto,
                                                              @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(ingredientService.updateIngredientById(id, ingredientDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredientById(@PathVariable(name = "id") long id) {
        ingredientService.deleteIngredientById(id);
        return new ResponseEntity<>("Ingredient with id " + id + " deleted successfully.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        return new ResponseEntity<>(ingredientService.getAllIngredients(), HttpStatus.OK);
    }
}
