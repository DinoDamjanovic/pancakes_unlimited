package com.dino_d.pancakes_unlimited.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PancakesUnlimitedAPIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public PancakesUnlimitedAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
