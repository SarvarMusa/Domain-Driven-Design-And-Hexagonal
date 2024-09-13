package com.food.order.system.order.service.messaging.listener.kafka;

import com.food.order.system.kafka.consumer.KafkaConsumer;
import com.food.order.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.order.system.kafka.order.avro.model.PaymentStatus;
import com.food.order.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.order.system.service.domain.ports.input.service.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {


    private final PaymentResponseMessageListener paymentResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public PaymentResponseKafkaListener(PaymentResponseMessageListener paymentResponseMessageListener, OrderMessagingDataMapper orderMessagingDataMapper) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}", topics = "${order-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} numberof payment response received with keys:{},partiitions:{},and offsets"
                , messages.size(), keys.toString(), partitions.toString(), offsets.toString());
        messages.forEach(paymentResponseAvroModel -> {
            if (PaymentStatus.COMPLETED == paymentResponseAvroModel.getPaymentStatus()) {
                log.info("Processing succesfully payment for order id:{}", paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener.paymentCompleted(
                        orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(
                                paymentResponseAvroModel
                        )
                );
            } else if (PaymentStatus.CANCELLED == paymentResponseAvroModel.getPaymentStatus()
                    || PaymentStatus.FAILED == paymentResponseAvroModel.getPaymentStatus()) {
                log.info("Processing unsuccesfully payment for order id:{}", paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener.paymentCanceled(
                        orderMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel)
                );
            }
        });
    }
}
