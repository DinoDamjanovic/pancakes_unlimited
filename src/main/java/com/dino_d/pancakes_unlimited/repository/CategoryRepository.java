package com.dino_d.pancakes_unlimited.repository;

import com.dino_d.pancakes_unlimited.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
