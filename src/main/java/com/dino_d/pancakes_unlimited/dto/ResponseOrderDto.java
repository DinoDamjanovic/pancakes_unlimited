package com.dino_d.pancakes_unlimited.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ResponseOrderDto {
    private long id;
    private String creationTime;
    private String description;
    private BigDecimal totalPrice;
    private List<ResponsePancakeOrderDto> pancakeList;
}
