package com.food.order.system.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class KafkaMessageHelper {

    public <T> ListenableFutureCallback<SendResult<String, T>> getKafkaCallBack(String responseTopicName,
                                                                                T requestAvroModel,
                                                                                String orderId,
                                                                                String requestAvroModelName) {
        return new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending PaymentRequestAvroModel"
                        + "message  {} to topic{}", requestAvroModelName, responseTopicName, ex
                );
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata recordMetadata = result.getRecordMetadata();
                log.info("Received succesfully response from kafka for order id:{}"
                                + "Topic:{} Partition:{} Offset:{},TimeStamp:{}",
                        orderId,
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset(),
                        recordMetadata.timestamp()
                );

            }
        };
    }
}
