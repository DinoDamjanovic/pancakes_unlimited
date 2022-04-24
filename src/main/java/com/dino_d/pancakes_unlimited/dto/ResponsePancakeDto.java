package com.dino_d.pancakes_unlimited.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponsePancakeDto {
    private long id;
    private List<String> ingredientsNames;
}
