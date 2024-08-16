package com.food.order.system.service.domain.mapper;

import com.food.order.system.domain.valueobject.CustomerId;
import com.food.order.system.domain.valueobject.Money;
import com.food.order.system.domain.valueobject.ProductId;
import com.food.order.system.domain.valueobject.RestaurantId;
import com.food.order.system.order.service.domain.entity.Order;
import com.food.order.system.order.service.domain.entity.OrderItem;
import com.food.order.system.order.service.domain.entity.Product;
import com.food.order.system.order.service.domain.entity.Restaurant;
import com.food.order.system.order.service.domain.valueobject.StreetAddress;
import com.food.order.system.order.service.domain.valueobject.TrackingId;
import com.food.order.system.service.domain.dto.create.CreateOrderCommand;
import com.food.order.system.service.domain.dto.create.CreateOrderResponse;
import com.food.order.system.service.domain.dto.create.OrderAddress;
import com.food.order.system.service.domain.dto.track.TrackOrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand command) {
        return Restaurant.builder().
                restaurantId(new RestaurantId(command.getRestaurantId()))
                .products(command.getOrderItems().stream().map(orderItem ->
                        new Product(new ProductId(orderItem.getProductId()))).collect(Collectors.toList()))
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand command) {
        return Order.builder()
                .customerId(new CustomerId(command.getCustomerId()))
                .restaurantId(new RestaurantId(command.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(command.getOrderAddress()))
                .price(new Money(command.getPrice()))
                .items(orderItemsToOrderItemsEntities(command.getOrderItems()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemsEntities(List<com.food.order.system.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> OrderItem.builder()
                .product(new Product(new ProductId(orderItem.getProductId())))
                .price(new Money(orderItem.getPrice()))
                .quantity(orderItem.getQuantity())
                .subTotal(new Money(orderItem.getSubTotal()))
                .build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity()
        );
    }
}
