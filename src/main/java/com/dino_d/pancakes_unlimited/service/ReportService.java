package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.IngredientReportDtoInt;

import java.util.List;

public interface ReportService {
    List<IngredientReportDtoInt> getIngredientsReport();
    List<IngredientReportDtoInt> getHealthyIngredientsReport();
}
