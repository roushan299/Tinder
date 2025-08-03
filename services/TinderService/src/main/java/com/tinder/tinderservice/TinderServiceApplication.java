package com.tinder.tinderservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TinderServiceApplication {

	public static void main(String[] args) {
		initializeEnvironmentVariables();
		SpringApplication.run(TinderServiceApplication.class, args);
	}

	private static void initializeEnvironmentVariables() {
		Dotenv dotenv = Dotenv.configure().load();
		dotenv.entries().forEach((entry) -> System.setProperty(entry.getKey(), entry.getValue()));
	}

}
