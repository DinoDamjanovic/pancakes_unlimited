package com.dino_d.pancakes_unlimited.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestOrderDto {
    private String description;
    private List<Long> pancakeIds;
}
