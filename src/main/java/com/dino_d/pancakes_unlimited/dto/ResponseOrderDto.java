package com.dino_d.pancakes_unlimited.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseOrderDto {
    private long id;
    private String creationTime;
    private String description;
    private List<ResponsePancakeDto> pancakeList;
}
