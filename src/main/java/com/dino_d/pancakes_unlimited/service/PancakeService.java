package com.dino_d.pancakes_unlimited.service;

import com.dino_d.pancakes_unlimited.dto.RequestPancakeDto;
import com.dino_d.pancakes_unlimited.dto.ResponsePancakeDto;

import java.util.List;

public interface PancakeService {
    ResponsePancakeDto createPancake(RequestPancakeDto requestPancakeDto);

    ResponsePancakeDto getPancakeById(long id);

    ResponsePancakeDto updatePancakeById(long id, RequestPancakeDto requestPancakeDto);

    void deletePancakeById(long id);

    List<ResponsePancakeDto> getAllPancakes();
}
