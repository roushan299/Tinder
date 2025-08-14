package com.tinder.tinderservice.messageservice;

import com.tinder.tinderservice.dto.SwipeMatchDTO;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class SwipeMatchKafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(SwipeMatchKafkaProducer.class);

    private final KafkaTemplate<String, SwipeMatchDTO> kafkaTemplate;
    private final String userCreatedTopic = "swipe-match-topic"; // change to your actual topic name

    public SwipeMatchKafkaProducer(KafkaTemplate<String, SwipeMatchDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSwipeMatch(SwipeMatchDTO swipeMatchDTO) {
        CompletableFuture<SendResult<String, SwipeMatchDTO>> future =
                kafkaTemplate.send(userCreatedTopic, swipeMatchDTO);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error("❌ Failed to send SwipeMatchDTO to topic [{}]: {}",
                        userCreatedTopic, ex.getMessage(), ex);
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                logger.info("✅ Sent SwipeMatchDTO to [{}], partition [{}], offset [{}]",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }
}

