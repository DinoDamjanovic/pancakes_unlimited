package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.RequestOrderDto;
import com.dino_d.pancakes_unlimited.dto.ResponseOrderDto;

import java.util.List;

public interface OrderService {
    ResponseOrderDto createOrder(RequestOrderDto requestOrderDto);

    ResponseOrderDto getOrderById(long id);

    List<ResponseOrderDto> getAllOrders();
}
