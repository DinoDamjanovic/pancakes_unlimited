package com.dino_d.pancakes_unlimited.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientDto {
    private long id;
    private String name;
    private BigDecimal price;
    private long categoryId;
}
