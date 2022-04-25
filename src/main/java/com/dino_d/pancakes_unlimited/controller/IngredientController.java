package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.RequestIngredientDto;
import com.dino_d.pancakes_unlimited.dto.ResponseIngredientDto;
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
    public ResponseEntity<ResponseIngredientDto> createIngredient(@RequestBody RequestIngredientDto requestIngredientDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(requestIngredientDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseIngredientDto> getIngredientById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(ingredientService.getIngredientById(id), HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ResponseIngredientDto>> getIngredientsByCategoryId(@PathVariable(name = "categoryId") long categoryId) {
        return new ResponseEntity<>(ingredientService.getIngredientsByCategoryId(categoryId), HttpStatus.OK);
    }

    //TODO Maybe this is a better solution than getIngredientsByCategoryId() method above?
//    @GetMapping
//    public List<IngredientDto> getIngredientsByCategoryIdQuery(@RequestParam(name = "categoryId") long categoryId) {
//        return ingredientService.getIngredientsByCategoryId(categoryId);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseIngredientDto> updateIngredientById(@RequestBody RequestIngredientDto requestIngredientDto,
                                                                      @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(ingredientService.updateIngredientById(id, requestIngredientDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredientById(@PathVariable(name = "id") long id) {
        ingredientService.deleteIngredientById(id);
        return new ResponseEntity<>("Ingredient with id " + id + " deleted successfully.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponseIngredientDto>> getAllIngredients() {
        return new ResponseEntity<>(ingredientService.getAllIngredients(), HttpStatus.OK);
    }
}
