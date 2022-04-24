package com.dino_d.pancakes_unlimited.repository;

import com.dino_d.pancakes_unlimited.entity.Pancake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PancakeRepository extends JpaRepository<Pancake, Long> {
}
