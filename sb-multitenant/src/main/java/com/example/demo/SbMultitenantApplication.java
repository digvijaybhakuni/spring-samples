package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SbMultitenantApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbMultitenantApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserRepository userRepository) {
		return args -> {
			userRepository.save(new User("JohnDoe", "john.doe@example.com"));
			userRepository.save(new User("JaneDoe", "jane.doe@example.com"));
		};
	}
}
