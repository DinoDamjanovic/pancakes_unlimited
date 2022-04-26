package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.RequestOrderDto;
import com.dino_d.pancakes_unlimited.dto.ResponseOrderDto;
import com.dino_d.pancakes_unlimited.dto.ResponsePancakeDto;
import com.dino_d.pancakes_unlimited.entity.Order;
import com.dino_d.pancakes_unlimited.entity.Pancake;
import com.dino_d.pancakes_unlimited.entity.PancakeIngredients;
import com.dino_d.pancakes_unlimited.exception.PancakesUnlimitedAPIException;
import com.dino_d.pancakes_unlimited.exception.ResourceNotFoundException;
import com.dino_d.pancakes_unlimited.repository.OrderRepository;
import com.dino_d.pancakes_unlimited.repository.PancakeRepository;
import com.dino_d.pancakes_unlimited.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.dino_d.pancakes_unlimited.utils.AppConstants.*;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private PancakeRepository pancakeRepository;

    public OrderServiceImpl(OrderRepository orderRepository, PancakeRepository pancakeRepository) {
        this.orderRepository = orderRepository;
        this.pancakeRepository = pancakeRepository;
    }

    @Override
    public ResponseOrderDto createOrder(RequestOrderDto requestOrderDto) {

        List<Long> pancakeIds = requestOrderDto.getPancakeIds();
        if (pancakeIds.size() < MINIMUM_PANCAKES_PER_ORDER) {
            throw new PancakesUnlimitedAPIException(HttpStatus.BAD_REQUEST,
                    "Minimum pancakes per order is " + MINIMUM_PANCAKES_PER_ORDER + ".");
        }

        List<Pancake> pancakes = getPancakes(pancakeIds);

        validatePancakes(pancakes);

        Order order = mapToEntity(requestOrderDto, new LinkedHashSet<>(pancakes));
        Order savedOrder = orderRepository.save(order);

        /*TODO cascade = CascadeType.ALL in Order class doesn't update order_id in pancakes table
           when saving order in database. This is a workaround.*/
        // ---------------------------------
        for (Pancake pancake : pancakes) {
            pancake.setOrder(savedOrder);
        }
        pancakeRepository.saveAll(pancakes);
        // ---------------------------------

        List<ResponsePancakeDto> pancakeDtos = pancakes.stream().map(
                p -> mapPancakeToDto(p)).collect(Collectors.toList());

        return mapToDto(savedOrder, pancakeDtos);
    }

    private List<Pancake> getPancakes(List<Long> pancakeIds) {
        List<Pancake> pancakes = pancakeRepository.findAllById(pancakeIds);

        // if not all provided pancakes exist in db, raise an exception
        if (pancakes.size() != pancakeIds.size()) {
            List<Long> validPancakeIds = pancakes.stream().map(p -> p.getId()).collect(Collectors.toList());

            for (Long pancakeId : pancakeIds) {
                if (!validPancakeIds.contains(pancakeId)) {
                    throw new ResourceNotFoundException("Pancake", "id", pancakeId);
                }
            }
        }

        return pancakes;
    }

    private void validatePancakes(List<Pancake> pancakes) {
        boolean hasBase = false;
        boolean hasStuffing = false;
        Set<PancakeIngredients> pancakeIngredients;

        for (Pancake pancake : pancakes) {
            pancakeIngredients = pancake.getPancakeIngredients();

            for (PancakeIngredients ingredient : pancakeIngredients) {
                if (ingredient.getIngredient().getCategory().getId() == CATEGORY_BASE_ID) {
                    hasBase = true;
                } else if (ingredient.getIngredient().getCategory().getId() == CATEGORY_STUFFING_ID) {
                    hasStuffing = true;
                }

                if (hasBase && hasStuffing) break;
            }

            if (!hasBase || !hasStuffing) {
                throw new PancakesUnlimitedAPIException(HttpStatus.BAD_REQUEST,
                        "Pancake with id " + pancake.getId() + " is incomplete. " +
                                "Pancake must have a base ingredient and at least one stuffing.");
            }
            hasBase = hasStuffing = false;
        }
    }

    private Order mapToEntity(RequestOrderDto requestOrderDto, Set<Pancake> pancakes) {
        Order order = new Order();
        order.setDescription(requestOrderDto.getDescription());
        order.setCreationTime(new Timestamp(new Date().getTime()));
        order.getPancakes().clear();
        order.getPancakes().addAll(pancakes);
        return order;
    }

    private ResponseOrderDto mapToDto(Order order, List<ResponsePancakeDto> pancakeDtos) {
        ResponseOrderDto responseOrderDto = new ResponseOrderDto();
        responseOrderDto.setId(order.getId());
        responseOrderDto.setCreationTime(String.valueOf(order.getCreationTime()));
        responseOrderDto.setDescription(order.getDescription());
        responseOrderDto.setPancakeList(pancakeDtos);
        return responseOrderDto;
    }

    private ResponsePancakeDto mapPancakeToDto(Pancake pancake) {
        ResponsePancakeDto responsePancakeDto = new ResponsePancakeDto();
        responsePancakeDto.setId(pancake.getId());

        List<String> ingredients = pancake.getPancakeIngredients().stream().map(
                p -> p.getIngredient().getName()).collect(Collectors.toList());
        responsePancakeDto.setIngredients(ingredients);

        return responsePancakeDto;
    }
}
