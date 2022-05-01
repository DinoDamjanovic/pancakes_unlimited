package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.ResponseIngredientReportInt;

import java.util.List;

public interface IngredientReportService {
    List<ResponseIngredientReportInt> getIngredientsReport();
    List<ResponseIngredientReportInt> getHealthyIngredientsReport();
}
