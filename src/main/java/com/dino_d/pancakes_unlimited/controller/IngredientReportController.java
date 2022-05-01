package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.ResponseIngredientReportInt;
import com.dino_d.pancakes_unlimited.service.IngredientReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class IngredientReportController {

    private IngredientReportService ingredientReportService;

    public IngredientReportController(IngredientReportService ingredientReportService) {
        this.ingredientReportService = ingredientReportService;
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<ResponseIngredientReportInt>> getIngredientsReport() {
        return new ResponseEntity<>(ingredientReportService.getIngredientsReport(), HttpStatus.OK);
    }

    @GetMapping("/healthy-ingredients")
    public ResponseEntity<List<ResponseIngredientReportInt>> getHealthyIngredientsReport() {
        return new ResponseEntity<>(ingredientReportService.getHealthyIngredientsReport(), HttpStatus.OK);
    }
}
