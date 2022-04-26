package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.RequestOrderDto;
import com.dino_d.pancakes_unlimited.dto.ResponseOrderDto;
import com.dino_d.pancakes_unlimited.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
