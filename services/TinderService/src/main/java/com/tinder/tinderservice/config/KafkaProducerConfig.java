package com.tinder.tinderservice.config;

import com.tinder.tinderservice.dto.SwipeMatchDTO;
import com.tinder.tinderservice.dto.UserDTO;
import com.tinder.tinderservice.dto.UserDeleteDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String KAFKA_URL;

    @Bean
    public ProducerFactory<String, UserDTO> producerFactory() {
        return new DefaultKafkaProducerFactory<>(
                Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_URL,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                        JsonSerializer.ADD_TYPE_INFO_HEADERS, false
                )
        );
    }

    @Bean
    public ProducerFactory<String, SwipeMatchDTO> producerFactorySwipeMatch() {
        return new DefaultKafkaProducerFactory<>(
                Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_URL,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                        JsonSerializer.ADD_TYPE_INFO_HEADERS, false
                )
        );
    }

    @Bean
    public ProducerFactory<String, UserDeleteDTO> producerFactoryUserDelete() {
        return new DefaultKafkaProducerFactory<>(
                Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_URL,
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                        JsonSerializer.ADD_TYPE_INFO_HEADERS, false
                )
        );
    }

    @Bean
    public KafkaTemplate<String, UserDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean KafkaTemplate<String, SwipeMatchDTO> kafkaTemplate2() {
        return  new KafkaTemplate<>(producerFactorySwipeMatch());
    }

    @Bean
    public  KafkaTemplate<String, UserDeleteDTO> kafkaTemplateUserDelete() {
        return new KafkaTemplate<>(producerFactoryUserDelete());
    }

}
