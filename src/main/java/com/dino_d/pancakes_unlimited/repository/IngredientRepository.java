package com.dino_d.pancakes_unlimited.repository;

import com.dino_d.pancakes_unlimited.dto.IngredientReportDtoInt;
import com.dino_d.pancakes_unlimited.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByCategoryId(long categoryId);

    @Query(value = "SELECT COUNT(*) AS numberOfOrders, ingredients.name AS name FROM ingredients\n" +
            "INNER JOIN pancakes_with_ingredients AS pwi ON pwi.ingredient_id = ingredients.id\n" +
            "INNER JOIN pancakes ON pancakes.id = pwi.pancake_id\n" +
            "INNER JOIN orders ON orders.id = pancakes.order_id\n" +
            "WHERE orders.creation_time > DATE_ADD(NOW(), INTERVAL -1 MONTH)\n" +
            "GROUP BY ingredients.id;",
            nativeQuery = true)
    List<IngredientReportDtoInt> getIngredientsList();

    @Query(value = "SELECT COUNT(*) AS numberOfOrders, ingredients.name AS name FROM ingredients\n" +
            "INNER JOIN pancakes_with_ingredients AS pwi ON pwi.ingredient_id = ingredients.id\n" +
            "INNER JOIN pancakes ON pancakes.id = pwi.pancake_id\n" +
            "INNER JOIN orders ON orders.id = pancakes.order_id\n" +
            "WHERE orders.creation_time > DATE_ADD(NOW(), INTERVAL -1 MONTH) AND ingredients.is_healthy=1\n" +
            "GROUP BY ingredients.id;",
            nativeQuery = true)
    List<IngredientReportDtoInt> getHealthyIngredientsList();
}
