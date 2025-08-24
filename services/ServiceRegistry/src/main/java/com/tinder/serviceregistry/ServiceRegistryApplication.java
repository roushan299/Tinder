package com.tinder.serviceregistry;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

	public static void main(String[] args) {
		initializeEnvironmentVariables();
        SpringApplication.run(ServiceRegistryApplication.class, args);
	}

    private static void initializeEnvironmentVariables() {
        Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach((entry) -> {System.setProperty(entry.getKey(), entry.getValue());});
    }

}
