package com.tinder.deckservice.config;

import com.tinder.deckservice.dto.UserDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, UserDTO> consumerFactory() {
        JsonDeserializer<UserDTO> deserializer = new JsonDeserializer<>(UserDTO.class);
        deserializer.setRemoveTypeHeaders(true);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092",
                        ConsumerConfig.GROUP_ID_CONFIG, "user-consumer-group",
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
                ),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
