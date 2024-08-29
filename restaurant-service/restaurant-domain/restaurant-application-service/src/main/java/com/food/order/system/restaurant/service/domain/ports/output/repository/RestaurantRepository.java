package com.food.order.system.restaurant.service.domain.ports.output.repository;

import com.food.order.system.restaurant.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
