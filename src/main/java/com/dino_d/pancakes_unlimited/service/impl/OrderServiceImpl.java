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

        validateOrder(requestOrderDto);

        List<Pancake> pancakes = getPancakes(requestOrderDto.getPancakeIds());
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

        return mapToDto(savedOrder);
    }

    @Override
    public ResponseOrderDto getOrderById(long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", id));
        return mapToDto(order);
    }

    @Override
    public List<ResponseOrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(o -> mapToDto(o)).collect(Collectors.toList());
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

    private void validateOrder(RequestOrderDto requestOrderDto) {
        if (requestOrderDto.getPancakeIds().size() < MINIMUM_PANCAKES_PER_ORDER) {
            throw new PancakesUnlimitedAPIException(HttpStatus.BAD_REQUEST,
                    "Minimum pancakes per order is " + MINIMUM_PANCAKES_PER_ORDER + ".");
        }
    }

    private void validatePancakes(List<Pancake> pancakes) {
        int baseIngredientCount = 0;
        boolean hasStuffing = false;
        Set<PancakeIngredients> pancakeIngredients;

        for (Pancake pancake : pancakes) {
            pancakeIngredients = pancake.getPancakeIngredients();

            for (PancakeIngredients ingredient : pancakeIngredients) {
                if (ingredient.getIngredient().getCategory().getId() == CATEGORY_BASE_ID) {
                    baseIngredientCount++;
                } else if (ingredient.getIngredient().getCategory().getId() == CATEGORY_STUFFING_ID) {
                    hasStuffing = true;
                }
            }

            if ((baseIngredientCount != 1) || !hasStuffing) {
                String stuffing = hasStuffing ? "has" : "doesn't have";
                throw new PancakesUnlimitedAPIException(HttpStatus.BAD_REQUEST,
                        "Pancake with id " + pancake.getId() + " is invalid. " +
                                "Pancake must have only one base ingredient and at least one stuffing. " +
                                "This pancake " + stuffing + " a stuffing and " +
                                "has " + baseIngredientCount + " base ingredient(s).");
            }

            baseIngredientCount = 0;
            hasStuffing = false;
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

    private ResponseOrderDto mapToDto(Order order) {
        ResponseOrderDto responseOrderDto = new ResponseOrderDto();
        responseOrderDto.setId(order.getId());
        responseOrderDto.setCreationTime(String.valueOf(order.getCreationTime()));
        responseOrderDto.setDescription(order.getDescription());

        List<ResponsePancakeDto> pancakeDtos = order.getPancakes().stream().map(
                p -> mapPancakeToDto(p)).collect(Collectors.toList());
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
