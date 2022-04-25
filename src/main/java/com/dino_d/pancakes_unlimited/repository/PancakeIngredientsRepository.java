package com.dino_d.pancakes_unlimited.repository;

import com.dino_d.pancakes_unlimited.entity.PancakeIngredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PancakeIngredientsRepository extends JpaRepository<PancakeIngredients, Long> {
    List<PancakeIngredients> findByPancakeId(long id);
    List<PancakeIngredients> findByIngredientId(long id);
    @Transactional
    long deleteByPancakeId(long id);
}
