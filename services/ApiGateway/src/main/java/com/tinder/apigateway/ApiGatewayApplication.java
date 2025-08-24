package com.tinder.apigateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		initializeEnvironmentVariables();
        SpringApplication.run(ApiGatewayApplication.class, args);
	}
    private static void initializeEnvironmentVariables() {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach((entry) -> {System.setProperty(entry.getKey(), entry.getValue());});
    }

}
