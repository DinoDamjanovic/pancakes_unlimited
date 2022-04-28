package com.dino_d.pancakes_unlimited.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class RequestIngredientDto {
    private long id;

    @Size(min = 2, message = "ingredient name should be minimum 2 characters")
    private String name;

    @NotNull
    @Min(value = 2, message = "minimum value for price is 0.00")
    private BigDecimal price;

    @NotNull
    private Long categoryId;

    @JsonProperty("isHealthy")
    @NotNull
    private Boolean isHealthy;
}
