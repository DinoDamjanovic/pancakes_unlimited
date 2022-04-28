package com.dino_d.pancakes_unlimited.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CategoryDto {
    private long id;

    @NotEmpty
    @Size(min = 2, message = "category name should be minimum 2 characters")
    private String name;
}

