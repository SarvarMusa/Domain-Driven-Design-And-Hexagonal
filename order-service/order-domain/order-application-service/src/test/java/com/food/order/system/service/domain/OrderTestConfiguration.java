package com.food.order.system.service.domain;

import com.food.order.system.order.service.domain.OrderDomainService;
import com.food.order.system.order.service.domain.OrderDomainServiceImpl;
import com.food.order.system.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.food.order.system.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMassagePublisher;
import com.food.order.system.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import com.food.order.system.service.domain.ports.output.repository.CustomerRepository;
import com.food.order.system.service.domain.ports.output.repository.OrderRepository;
import com.food.order.system.service.domain.ports.output.repository.RestaurantRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.food.order.system")
public class OrderTestConfiguration {

    @Bean
    public OrderCreatedPaymentRequestMassagePublisher orderCreatedPaymentRequestMassagePublisher() {
        return Mockito.mock(OrderCreatedPaymentRequestMassagePublisher.class);
    }

    @Bean
    public OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMassagePublisher() {
        return Mockito.mock(OrderCancelledPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher() {
        return Mockito.mock(OrderPaidRestaurantRequestMessagePublisher.class);
    }

    @Bean
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return Mockito.mock(RestaurantRepository.class);
    }

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }

}
