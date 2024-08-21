package com.food.order.system.order.service.messaging.publisher.kafka;

import com.food.order.system.kafka.producer.service.KafkaProducer;
import com.food.order.system.order.service.domain.event.OrderPaidEvent;
import com.food.order.system.kafka.order.avro.model.*;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.order.system.service.domain.config.OrderServiceConfigData;
import com.food.order.system.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements OrderPaidRestaurantRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderKafkaMessageHelper orderKafkaMessageHelper;

    public PayOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                         OrderServiceConfigData orderServiceConfigData,
                                         KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
                                         OrderKafkaMessageHelper orderKafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.orderKafkaMessageHelper = orderKafkaMessageHelper;
    }

    @Override

    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        try {

            RestaurantApprovalRequestAvroModel requestAvroModel =
                    orderMessagingDataMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(
                            domainEvent
                    );
            kafkaProducer.send(
                    orderServiceConfigData.getRestaurantApprovalTopicName(),
                    orderId,
                    requestAvroModel,
                    orderKafkaMessageHelper.getKafkaCallBack(
                            orderServiceConfigData.getRestaurantApprovalTopicName(),
                            requestAvroModel,
                            orderId,
                            "RestaurantApprovalRequestAvroModel"
                    )
            );
            log.info("RestaurantApprovalRequestAvroModel sent to for order id:{}", orderId);
        } catch (Exception e) {
            log.error("Error while sending RestaurantApprovalRequestAvroModel message"
                    + "to kafka with order id:{},error:{}", orderId, e.getMessage());

        }

    }
}
