package com.dino_d.pancakes_unlimited.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestIngredientDto {
    private long id;
    private String name;
    private BigDecimal price;
    private long categoryId;
    @JsonProperty("isHealthy")
    private boolean isHealthy;
}
