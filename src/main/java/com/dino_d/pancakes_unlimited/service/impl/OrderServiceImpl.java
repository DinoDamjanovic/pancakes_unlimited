package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.RequestOrderDto;
import com.dino_d.pancakes_unlimited.dto.ResponseOrderDto;
import com.dino_d.pancakes_unlimited.dto.ResponsePancakeOrderDto;
import com.dino_d.pancakes_unlimited.entity.Ingredient;
import com.dino_d.pancakes_unlimited.entity.Order;
import com.dino_d.pancakes_unlimited.entity.Pancake;
import com.dino_d.pancakes_unlimited.exception.PancakesUnlimitedAPIException;
import com.dino_d.pancakes_unlimited.exception.ResourceNotFoundException;
import com.dino_d.pancakes_unlimited.repository.OrderRepository;
import com.dino_d.pancakes_unlimited.repository.PancakeRepository;
import com.dino_d.pancakes_unlimited.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;
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
        List<Pancake> pancakes = validateOrder(requestOrderDto);

        Order order = mapToEntity(requestOrderDto, new LinkedHashSet<>(pancakes));
        Order savedOrder = orderRepository.save(order);

        for (Pancake pancake : pancakes) {
            pancake.setOrder(savedOrder);
        }
        pancakeRepository.saveAll(pancakes);

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

    @Override
    public String getPopularIngredients() {
        return "ADMIN";
    }

    private List<Pancake> validateOrder(RequestOrderDto requestOrderDto) {

        // check how many pancakes is in the order
        List<Long> pancakeIds = requestOrderDto.getPancakeIds();
        if (pancakeIds.size() < MINIMUM_PANCAKES_PER_ORDER) {
            throw new PancakesUnlimitedAPIException(HttpStatus.BAD_REQUEST,
                    "Minimum pancakes per order is " + MINIMUM_PANCAKES_PER_ORDER + ".");
        }

        // check if all pancakes have exactly one base ingredient and minimum one stuffing
        List<Pancake> pancakes = getPancakes(pancakeIds);
        int baseIngredientCount = 0;
        boolean hasStuffing = false;
        Set<Ingredient> ingredients;

        for (Pancake pancake : pancakes) {
            ingredients = pancake.getIngredients();

            for (Ingredient ingredient : ingredients) {
                if (ingredient.getCategory().getId() == CATEGORY_BASE_ID) baseIngredientCount++;
                if (ingredient.getCategory().getId() == CATEGORY_STUFFING_ID) hasStuffing = true;
            }

            if ((baseIngredientCount != 1) || !hasStuffing) {
                String stuffing = hasStuffing ? "has" : "doesn't have";
                throw new PancakesUnlimitedAPIException(HttpStatus.BAD_REQUEST,
                        "Pancake with id " + pancake.getId() + " is invalid. " +
                                "Pancake must have only one base ingredient and at least one stuffing. " +
                                "This pancake " + stuffing + " a stuffing and " +
                                "it has " + baseIngredientCount + " base ingredient(s).");
            }

            baseIngredientCount = 0;
            hasStuffing = false;
        }
        // return list of pancakes for further processing
        return pancakes;
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

    private BigDecimal calculateOrderPrice(Order order) {

        Set<Pancake> pancakes = order.getPancakes();
        BigDecimal totalPrice = pancakes.stream().map(
                p -> p.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);

        // first calculate 5% and 10% discounts if applicable
        BigDecimal discountModifier = DISCOUNT_0_PERCENT;
        if (totalPrice.compareTo(BigDecimal.valueOf(200)) > 0) {
            discountModifier = DISCOUNT_10_PERCENT;
        } else if (totalPrice.compareTo(BigDecimal.valueOf(100)) > 0) {
            discountModifier = DISCOUNT_5_PERCENT;
        }
        totalPrice = totalPrice.multiply(discountModifier);

        // now calculate healthy pancakes discount if applicable
        // we will store ids of healthy pancakes into a List to apply discount to them if needed
        List<Long> healthyPancakeIds = new ArrayList<>();

        BigDecimal healthyDiscountedPrice = BigDecimal.valueOf(0);
        BigDecimal newPrice;

        for (Pancake pancake : pancakes) {

            if (isHealthy(pancake)) {
                newPrice = pancake.getPrice().multiply(DISCOUNT_15_PERCENT);
                healthyPancakeIds.add(pancake.getId());
            } else {
                newPrice = pancake.getPrice();
            }
            healthyDiscountedPrice = healthyDiscountedPrice.add(newPrice);
        }

        // if healthy discount is higher than a 5% or 10% discount, apply it to the order totalPrice
        // also apply prices to healthy pancakes and save new pancake prices to db
        if (totalPrice.compareTo(healthyDiscountedPrice) > 0) {
            totalPrice = healthyDiscountedPrice;
            savePancakePricesToDB(pancakes, healthyPancakeIds);

            // else apply 5% or 10% discount to all pancakes in the order and save them
        } else {
            savePancakePricesToDB(pancakes, discountModifier);
        }

        // round to 2 decimal places
        return totalPrice.setScale(2, RoundingMode.HALF_EVEN);
    }

    private boolean isHealthy(Pancake pancake) {
        int totalIngredients = 0;
        int healthyIngredients = 0;

        for (Ingredient ingredient : pancake.getIngredients()) {
            if (ingredient.getIsHealthy()) {
                healthyIngredients++;
            }
            totalIngredients++;
        }

        // check if percentage of healthy ingredients is higher than HEALTHY_DISCOUNT_THRESHOLD
        return (((float) healthyIngredients / totalIngredients) * 100) > HEALTHY_DISCOUNT_THRESHOLD;
    }

    private void savePancakePricesToDB(Set<Pancake> pancakes, List<Long> healthyPancakeIds) {
        for (Pancake pancake : pancakes) {
            if (healthyPancakeIds.contains(pancake.getId())) {
                pancake.setPrice(pancake.getPrice().multiply(DISCOUNT_15_PERCENT)
                        .setScale(2, RoundingMode.HALF_EVEN));
                pancakeRepository.save(pancake);
            }
        }
    }

    private void savePancakePricesToDB(Set<Pancake> pancakes, BigDecimal discountModifier) {
        for (Pancake pancake : pancakes) {
            pancake.setPrice(pancake.getPrice().multiply(discountModifier)
                    .setScale(2, RoundingMode.HALF_EVEN));
        }
        pancakeRepository.saveAll(pancakes);
    }

    private Order mapToEntity(RequestOrderDto requestOrderDto, Set<Pancake> pancakes) {
        Order order = new Order();
        order.getPancakes().addAll(pancakes);
        order.setDescription(requestOrderDto.getDescription());
        order.setTotalPrice(calculateOrderPrice(order));
        order.setCreationTime(new Timestamp(new Date().getTime()));

        return order;
    }

    private ResponseOrderDto mapToDto(Order order) {
        ResponseOrderDto responseOrderDto = new ResponseOrderDto();
        responseOrderDto.setId(order.getId());
        responseOrderDto.setCreationTime(String.valueOf(order.getCreationTime()));
        responseOrderDto.setDescription(order.getDescription());
        responseOrderDto.setTotalPrice(order.getTotalPrice());

        List<ResponsePancakeOrderDto> pancakeDtos = order.getPancakes().stream().map(
                p -> mapPancakeToDto(p)).collect(Collectors.toList());
        responseOrderDto.setPancakeList(pancakeDtos);

        return responseOrderDto;
    }

    private ResponsePancakeOrderDto mapPancakeToDto(Pancake pancake) {
        ResponsePancakeOrderDto responsePancakeOrderDto = new ResponsePancakeOrderDto();
        responsePancakeOrderDto.setId(pancake.getId());

        List<String> ingredients = pancake.getIngredients().stream().map(
                i -> i.getName()).collect(Collectors.toList());
        responsePancakeOrderDto.setIngredients(ingredients);

        responsePancakeOrderDto.setPrice(pancake.getPrice());

        return responsePancakeOrderDto;
    }
}
