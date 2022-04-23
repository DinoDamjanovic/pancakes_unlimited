package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.IngredientDto;
import com.dino_d.pancakes_unlimited.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class IngredientController {

    IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping("categories/{categoryId}/ingredients")
    public ResponseEntity<IngredientDto> createIngredient(@PathVariable(name = "categoryId") long id,
                                                          @RequestBody IngredientDto ingredientDto) {
        return new ResponseEntity<>(ingredientService.createIngredient(id, ingredientDto), HttpStatus.CREATED);
    }

}
