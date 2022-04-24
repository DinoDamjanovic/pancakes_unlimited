package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.RequestPancakeDto;
import com.dino_d.pancakes_unlimited.dto.ResponsePancakeDto;

public interface PancakeService {
    ResponsePancakeDto createPancake(RequestPancakeDto requestPancakeDto);
}
