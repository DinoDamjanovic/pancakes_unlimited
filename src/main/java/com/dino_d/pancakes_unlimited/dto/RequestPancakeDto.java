package com.dino_d.pancakes_unlimited.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestPancakeDto {
    private long id;
    private List<Long> ingredientIds;
}
