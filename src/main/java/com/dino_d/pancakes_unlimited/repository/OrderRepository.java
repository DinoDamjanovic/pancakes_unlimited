package com.dino_d.pancakes_unlimited.repository;

import com.dino_d.pancakes_unlimited.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
