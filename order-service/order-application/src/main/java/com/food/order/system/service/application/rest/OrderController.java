package com.food.order.system.service.application.rest;

import com.food.order.system.service.domain.dto.create.CreateOrderCommand;
import com.food.order.system.service.domain.dto.create.CreateOrderResponse;
import com.food.order.system.service.domain.dto.track.TrackOrderQuery;
import com.food.order.system.service.domain.dto.track.TrackOrderResponse;
import com.food.order.system.service.domain.ports.input.service.OrderApplicationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vdn.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand command) {
        log.info("Creating order for customer:{} at restaurant:{}", command.getCustomerId(), command.getRestaurantId());
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(command);
        log.info("Order created with tracking id:{}", createOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse =
                orderApplicationService.trackOrder(TrackOrderQuery.builder().orderTrackingId(trackingId).build());
        log.info("Returning order status with tracking id:{}", trackOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(trackOrderResponse);
    }
}
