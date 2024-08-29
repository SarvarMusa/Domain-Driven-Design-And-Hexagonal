package com.food.order.system.restaurant.service.domain.mapper;

import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.domain.valueobject.OrderStatus;
import com.food.order.system.domain.valueobject.RestaurantId;
import com.food.order.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.service.domain.entity.OrderDetail;
import com.food.order.system.restaurant.service.domain.entity.Product;
import com.food.order.system.restaurant.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataMapper {

    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest approvalRequest) {
        return Restaurant.
                builder()
                .id(new RestaurantId(UUID.fromString(approvalRequest.getRestaurantId())))
                .orderDetail(OrderDetail
                        .builder()
                        .id(new OrderId(UUID.fromString(approvalRequest.getOrderId())))
                        .products(approvalRequest.getProducts()
                                .stream()
                                .map(product -> Product.builder()
                                        .quantity(product.getQuantity())
                                        .id(product.getId())
                                        .build()
                                ).collect(Collectors.toList())
                        ).totalAmount(new Money(approvalRequest.getPrice()))
                        .orderStatus(OrderStatus.valueOf(approvalRequest.getRestaurantOrderStatus().name())
                        ).build()).build();

    }
}
