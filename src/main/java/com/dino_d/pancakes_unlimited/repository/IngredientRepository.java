package com.dino_d.pancakes_unlimited.repository;

import com.dino_d.pancakes_unlimited.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByCategoryId(long categoryId);
}
