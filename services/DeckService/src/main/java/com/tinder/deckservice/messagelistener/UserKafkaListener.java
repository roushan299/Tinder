package com.tinder.deckservice.messagelistener;

import com.tinder.deckservice.dto.UserDTO;
import com.tinder.deckservice.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserKafkaListener {

    private static final Logger logger = LoggerFactory.getLogger(UserKafkaListener.class);
    private final IUserService userService;

    public UserKafkaListener(IUserService userService) {
        this.userService = userService;
    }

    @KafkaListener(
            topics = "${app.kafka.topic.user-created}",
            groupId = "user-consumer-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenUserCreated(UserDTO userDTO) {
        logger.info("📥 Received UserDTO from Kafka: {}", userDTO);

        // TODO: Process userDTO (e.g., save to DB, send notification, etc.)
        userService.saveUser(userDTO);

    }
}
