package com.dino_d.pancakes_unlimited.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @Size(min = 2, message = "username should be minimum 2 characters")
    private String username;

    @Size(min = 4, message = "password should be minimum 4 characters")
    private String password;
}
