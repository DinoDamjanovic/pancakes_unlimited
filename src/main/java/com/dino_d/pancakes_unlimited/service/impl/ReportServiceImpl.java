package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.IngredientReportDtoInt;
import com.dino_d.pancakes_unlimited.repository.IngredientRepository;
import com.dino_d.pancakes_unlimited.service.ReportService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private IngredientRepository ingredientRepository;

    public ReportServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<IngredientReportDtoInt> getIngredientsReport() {
        List<IngredientReportDtoInt> ingredients = ingredientRepository.getIngredientsList();
        return getMostOrderedIngredients(ingredients);
    }

    @Override
    public List<IngredientReportDtoInt> getHealthyIngredientsReport() {
        List<IngredientReportDtoInt> ingredients = ingredientRepository.getHealthyIngredientsList();
        return getMostOrderedIngredients(ingredients);
    }

    private List<IngredientReportDtoInt> getMostOrderedIngredients(List<IngredientReportDtoInt> ingredients) {
        List<IngredientReportDtoInt> mostOrderedIngredients = new ArrayList<>();

        long maxOrders = 0;
        long orders;

        for (IngredientReportDtoInt ingredient : ingredients) {
            orders = ingredient.getNumberOfOrders();

            if (orders > maxOrders) {
                maxOrders = orders;
                mostOrderedIngredients.clear();
                mostOrderedIngredients.add(ingredient);

            } else if (orders == maxOrders) {
                mostOrderedIngredients.add(ingredient);
            }
        }

        return mostOrderedIngredients;
    }
}
