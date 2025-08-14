package com.tinder.deckservice.messagelistener;

import com.tinder.deckservice.dto.SwipeMatchDTO;
import com.tinder.deckservice.service.ISwipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SwipeMatchKafkaListener {

    private static final Logger logger = LoggerFactory.getLogger(SwipeMatchKafkaListener.class);
    private final ISwipeService swipeService;



    public SwipeMatchKafkaListener(ISwipeService swipeService) {
        this.swipeService = swipeService;
    }


    @KafkaListener(
            topics = "swipe-match-topic",
            groupId = "swipe-match-consumer-group",
            containerFactory = "swipeMatchKafkaListenerContainerFactory"
    )
    public void listenSwipeMatch(SwipeMatchDTO swipeMatchDTO) {
        System.out.println("📥 Received SwipeMatchDTO: " + swipeMatchDTO);
        swipeService.createSwipeAndMatch(swipeMatchDTO);
    }
}
