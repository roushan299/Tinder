package com.tinder.tinderservice.messageservice;

import com.tinder.tinderservice.dto.UserDeleteDTO;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class UserDeleteKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(UserDeleteKafkaProducer.class);

    private final KafkaTemplate<String, UserDeleteDTO>  kafkaTemplate;
    private final String userDeleteTopic = "user-delete-topic";


    public UserDeleteKafkaProducer(KafkaTemplate<String, UserDeleteDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void deleteUser(UserDeleteDTO userDeleteDTO) {
        log.info("Preparing to send user delete event for UUID: {}", userDeleteDTO.getUuid());

        CompletableFuture<SendResult<String, UserDeleteDTO>> future =
                kafkaTemplate.send(userDeleteTopic, userDeleteDTO.getUuid(), userDeleteDTO);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to send delete event for UUID: {}. Error: {}",
                        userDeleteDTO.getUuid(), ex.getMessage(), ex);
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Successfully sent delete event for UUID: {} to topic: {} | partition: {} | offset: {}",
                        userDeleteDTO.getUuid(),
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset());
            }
        });
    }

}
