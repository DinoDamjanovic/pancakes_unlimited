package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.*;
import com.dino_d.pancakes_unlimited.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ResponseOrderDto> createOrder(@RequestBody RequestOrderDto requestOrderDto) {
        return new ResponseEntity<>(orderService.createOrder(requestOrderDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseOrderDto> getOrderById(@PathVariable(name = "id") @Min(1) long id) {
        return new ResponseEntity<>(orderService.getOrderById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ResponseOrderDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/reports/ingredients")
    public ResponseEntity<String> getOrderById() {
        return new ResponseEntity<>(orderService.getPopularIngredients(), HttpStatus.OK);
    }
}
