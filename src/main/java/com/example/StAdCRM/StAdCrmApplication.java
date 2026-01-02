package com.example.StAdCRM;

import com.example.StAdCRM.entity.Role;
import com.example.StAdCRM.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StAdCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(StAdCrmApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.createUserIfNotExist("admin@crm.com", "admin", Role.ADMIN);
			userService.createUserIfNotExist("counselor@crm.com", "counselor", Role.COUNSELOR);
			userService.createUserIfNotExist("finance@crm.com", "finance", Role.FINANCE);
			userService.createUserIfNotExist("verifier@crm.com", "verifier", Role.VERIFIER);
		};
	}

}
