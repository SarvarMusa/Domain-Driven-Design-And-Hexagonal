package com.food.order.system.restaurant.service.domain;

import com.food.order.system.domain.valueobject.OrderId;
import com.food.order.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.order.system.restaurant.service.domain.entity.Product;
import com.food.order.system.restaurant.service.domain.entity.Restaurant;
import com.food.order.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.order.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.food.order.system.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.food.order.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.order.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.food.order.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import com.food.order.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component

public class RestaurantApprovalRequestHelper {
    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final RestaurantRepository restaurantRepository;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;
    private final OrderApprovalRepository orderApprovalRepository;

    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantDataMapper restaurantDataMapper,
                                           OrderApprovedMessagePublisher orderApprovedMessagePublisher,
                                           RestaurantRepository restaurantRepository,
                                           OrderRejectedMessagePublisher orderRejectedMessagePublisher, OrderApprovalRepository orderApprovalRepository) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantDataMapper = restaurantDataMapper;
        this.orderApprovedMessagePublisher = orderApprovedMessagePublisher;
        this.restaurantRepository = restaurantRepository;
        this.orderRejectedMessagePublisher = orderRejectedMessagePublisher;
        this.orderApprovalRepository = orderApprovalRepository;
    }

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest approvalRequest) {
        log.info("Processing restaurant approval for order id: {}", approvalRequest.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(approvalRequest);
        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurant,
                failureMessages,
                orderApprovedMessagePublisher,
                orderRejectedMessagePublisher);

        orderApprovalRepository.save(restaurant.getOrderApproval());
        return orderApprovalEvent;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest approvalRequest) {
        Restaurant restaurant = restaurantDataMapper.restaurantApprovalRequestToRestaurant(approvalRequest);
        Optional<Restaurant> restaurantOptional = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantOptional.isEmpty()) {
            log.error("Restaurant with id: " + restaurant.getId().getValue() + "not found");
            throw new RestaurantNotFoundException("Restaurant with id: " + restaurant.getId().getValue() + "not found");
        }
        Restaurant restaurantEntity = restaurantOptional.get();
        restaurant.setActive(restaurantEntity.isActive());
        for (Product product : restaurant.getOrderDetail().getProducts()) {
            restaurantEntity.getOrderDetail().getProducts().forEach(p -> {
                if (p.getId().equals(product.getId())) {
                    product.updateWithConfirmedNamePriceAndAvailability(p.getName(), p.getPrice(), p.isAvailable());
                }
            });
        }
        restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(approvalRequest.getOrderId())));
        return restaurant;
    }

}

