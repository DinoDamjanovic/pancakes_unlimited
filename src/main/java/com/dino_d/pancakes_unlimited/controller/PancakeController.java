package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.RequestPancakeDto;
import com.dino_d.pancakes_unlimited.dto.ResponsePancakeDto;
import com.dino_d.pancakes_unlimited.service.PancakeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pancakes")
public class PancakeController {

    private PancakeService pancakeService;

    public PancakeController(PancakeService pancakeService) {
        this.pancakeService = pancakeService;
    }

    @PostMapping
    public ResponseEntity<ResponsePancakeDto> createPancake(@RequestBody RequestPancakeDto requestPancakeDto) {
        return new ResponseEntity<>(pancakeService.createPancake(requestPancakeDto), HttpStatus.CREATED);
    }
}
