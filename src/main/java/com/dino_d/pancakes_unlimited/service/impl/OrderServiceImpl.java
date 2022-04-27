package com.dino_d.pancakes_unlimited.service.impl;

import com.dino_d.pancakes_unlimited.dto.RequestOrderDto;
import com.dino_d.pancakes_unlimited.dto.ResponseOrderDto;
import com.dino_d.pancakes_unlimited.dto.ResponsePancakeOrderDto;
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

        /*
        TODO cascade = CascadeType.ALL in Order class doesn't update order_id
         in pancakes table when saving order in database. This is a workaround.
        */
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
        if (totalPrice.compareTo(BigDecimal.valueOf(200)) > 0) {
            totalPrice = totalPrice.multiply(DISCOUNT_10_PERCENT);

        } else if (totalPrice.compareTo(BigDecimal.valueOf(100)) > 0) {
            totalPrice = totalPrice.multiply(DISCOUNT_5_PERCENT);
        }

        // now calculate healthy pancakes discount if applicable

        // we will store ids and new prices of healthy pancakes to update them if needed
        // Long = pancake_id     BigDecimal = new price
        Map<Long, BigDecimal> discountedPancakePrices = new HashMap<>();

        BigDecimal healthyDiscountedPrice = BigDecimal.valueOf(0);
        BigDecimal newPrice;
        int totalIngredients = 0;
        int healthyIngredients = 0;

        for (Pancake pancake : pancakes) {

            for (PancakeIngredients pancakeIngredient : pancake.getPancakeIngredients()) {
                if (pancakeIngredient.getIngredient().isHealthy()) {
                    healthyIngredients++;
                }
                totalIngredients++;
            }

            // check if percentage of healthy ingredients is higher than HEALTHY_DISCOUNT_THRESHOLD
            if ((((float) healthyIngredients / totalIngredients) * 100) > HEALTHY_DISCOUNT_THRESHOLD) {
                newPrice = pancake.getPrice().multiply(DISCOUNT_15_PERCENT);
                discountedPancakePrices.put(pancake.getId(), newPrice);
            } else {
                newPrice = pancake.getPrice();
            }

            healthyDiscountedPrice = healthyDiscountedPrice.add(newPrice);
        }

        // if healthy discount is higher than a 5% or 10% discount, apply it to the order totalPrice
        // also apply prices to healthy pancakes and save new pancake prices to db
        if (totalPrice.compareTo(healthyDiscountedPrice) > 0) {
            totalPrice = healthyDiscountedPrice;
            savePancakePricesToDB(pancakes, discountedPancakePrices);
        }

        // round to 2 decimal places
        return totalPrice.setScale(2, RoundingMode.HALF_EVEN);
    }

    private void savePancakePricesToDB(Set<Pancake> pancakes, Map<Long, BigDecimal> discountedPancakePrices) {
        for (Map.Entry<Long, BigDecimal> entry : discountedPancakePrices.entrySet()) {
            for (Pancake pancake : pancakes) {
                if (entry.getKey() == pancake.getId()) {
                    pancake.setPrice(entry.getValue().setScale(2, RoundingMode.HALF_EVEN));
                    pancakeRepository.save(pancake);
                }
            }
        }
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

        List<String> ingredients = pancake.getPancakeIngredients().stream().map(
                p -> p.getIngredient().getName()).collect(Collectors.toList());
        responsePancakeOrderDto.setIngredients(ingredients);

        responsePancakeOrderDto.setPrice(pancake.getPrice());

        return responsePancakeOrderDto;
    }
}
