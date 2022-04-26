package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.RequestOrderDto;
import com.dino_d.pancakes_unlimited.dto.ResponseOrderDto;

public interface OrderService {
    ResponseOrderDto createOrder(RequestOrderDto requestOrderDto);
}
