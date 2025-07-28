package com.tinder.tinderservice.messageservice;

import com.tinder.tinderservice.dto.UserDTO;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class UserKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(UserKafkaProducer.class);

    private final KafkaTemplate<String, UserDTO> kafkaTemplate;

    @Value("${app.kafka.topic.user-created}")
    private String userCreatedTopic;

    public UserKafkaProducer(KafkaTemplate<String, UserDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUser(UserDTO userDTO) {
        CompletableFuture<SendResult<String, UserDTO>> future =
                kafkaTemplate.send(userCreatedTopic, userDTO);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("❌ Failed to send UserDTO to topic [{}]: {}", userCreatedTopic, ex.getMessage(), ex);
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                logger.info("✅ Sent UserDTO to [{}], partition [{}], offset [{}]",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }
}
