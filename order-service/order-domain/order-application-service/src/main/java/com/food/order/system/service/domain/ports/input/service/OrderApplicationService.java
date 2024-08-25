package com.food.order.system.service.domain.ports.input.service;

import com.food.order.system.service.domain.dto.create.CreateOrderCommand;
import com.food.order.system.service.domain.dto.create.CreateOrderResponse;
import com.food.order.system.service.domain.dto.track.TrackOrderQuery;
import com.food.order.system.service.domain.dto.track.TrackOrderResponse;

import javax.swing.*;
import javax.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand command);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery query);
}
