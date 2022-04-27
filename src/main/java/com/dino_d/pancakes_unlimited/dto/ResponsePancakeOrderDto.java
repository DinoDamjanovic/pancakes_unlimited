package com.dino_d.pancakes_unlimited.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ResponsePancakeOrderDto {
    private long id;
    private BigDecimal price;
    private List<String> ingredients;
}
