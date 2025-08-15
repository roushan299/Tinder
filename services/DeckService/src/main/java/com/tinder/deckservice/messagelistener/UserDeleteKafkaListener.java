package com.tinder.deckservice.messagelistener;

import com.tinder.deckservice.dto.UserDeleteDTO;
import com.tinder.deckservice.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class UserDeleteKafkaListener {

    private static final Logger logger = LoggerFactory.getLogger(UserDeleteKafkaListener.class);
    private final UserUtil userUtil;

    public UserDeleteKafkaListener(UserUtil userUtil) {
        this.userUtil = userUtil;
    }


    @KafkaListener(
            topics = "user-delete-topic",
            groupId = "user-delete-consumer-group",
            containerFactory = "userDeleteKafkaListenerContainerFactory"
    )
    public void listenUserDelete(UserDeleteDTO userDeleteDTO) {
        logger.info("Received UserDelete event for UUID: {}", userDeleteDTO.getUuid());

        try {
            userUtil.deleteUser(userDeleteDTO.getUuid());
            logger.info("Successfully processed UserDelete event for UUID: {}", userDeleteDTO.getUuid());
        } catch (Exception e) {
            logger.error("Error while processing UserDelete event for UUID: {}", userDeleteDTO.getUuid(), e);
            // Optionally: rethrow to trigger Kafka retry or DLQ
            throw e;
        }
    }

}
