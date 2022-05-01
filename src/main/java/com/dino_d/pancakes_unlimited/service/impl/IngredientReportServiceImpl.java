package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.ResponseIngredientReportInt;
import com.dino_d.pancakes_unlimited.repository.IngredientReportRepository;
import com.dino_d.pancakes_unlimited.service.IngredientReportService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientReportServiceImpl implements IngredientReportService {

    private IngredientReportRepository ingredientReportRepository;

    public IngredientReportServiceImpl(IngredientReportRepository ingredientReportRepository) {
        this.ingredientReportRepository = ingredientReportRepository;
    }

    @Override
    public List<ResponseIngredientReportInt> getIngredientsReport() {
        List<ResponseIngredientReportInt> ingredients = ingredientReportRepository.getIngredientsList();
        return getMostOrderedIngredients(ingredients);
    }

    @Override
    public List<ResponseIngredientReportInt> getHealthyIngredientsReport() {
        List<ResponseIngredientReportInt> ingredients = ingredientReportRepository.getHealthyIngredientsList();
        return getMostOrderedIngredients(ingredients);
    }

    private List<ResponseIngredientReportInt> getMostOrderedIngredients(List<ResponseIngredientReportInt> ingredients) {
        List<ResponseIngredientReportInt> mostOrderedIngredients = new ArrayList<>();

        long maxOrders = 0;
        long orders;

        for (ResponseIngredientReportInt ingredient : ingredients) {
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
