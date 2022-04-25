package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.RequestPancakeDto;
import com.dino_d.pancakes_unlimited.dto.ResponsePancakeDto;
import com.dino_d.pancakes_unlimited.service.PancakeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePancakeDto> getPancakeById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(pancakeService.getPancakeById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePancakeDto> updatePancakeById(@PathVariable(name = "id") long id,
                                                                @RequestBody RequestPancakeDto requestPancakeDto) {
        return new ResponseEntity<>(pancakeService.updatePancakeById(id, requestPancakeDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePancakeById(@PathVariable(name = "id") long id) {
        pancakeService.deletePancakeById(id);
        return new ResponseEntity<>("Pancake with id " + id + " deleted successfully.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponsePancakeDto>> getAllPancakes() {
        return new ResponseEntity<>(pancakeService.getAllPancakes(), HttpStatus.OK);
    }
}
