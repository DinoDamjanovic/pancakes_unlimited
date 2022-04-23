package com.dino_d.pancakes_unlimited.repository;

import com.dino_d.pancakes_unlimited.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
