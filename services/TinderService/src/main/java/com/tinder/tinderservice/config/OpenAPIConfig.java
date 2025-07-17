package com.tinder.tinderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI apiInfo(){
        return new OpenAPI()
                .info(new Info().title("Tinder Service")
                        .description("Tinder Service")
                        .version("1.0"));
    }
}
