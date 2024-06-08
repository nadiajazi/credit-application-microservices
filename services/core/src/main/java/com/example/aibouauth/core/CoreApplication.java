package com.example.aibouauth.core;

import com.example.aibouauth.core.auth.AuthenticationService;
import com.example.aibouauth.core.auth.RegisterRequest;
import com.example.aibouauth.core.user.Role;
import com.example.aibouauth.core.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@AllArgsConstructor
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}
	private  final UserRepository userRepository;


	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstName("admin")
					.lastName("admin")
					.email("admin@mail.com")
					.password("password")
					.role(Role.ADMIN)
					.phone("99999999")

					.build();

			// Check if the user with the same email already exists
			if (userRepository.findUserByEmail(admin.getEmail()).isEmpty()) {
				System.out.println("Admin token: " + service.register(admin).getAccessToken());
			} else {
				//System.out.println("User with email 'admin@mail.com' already exists.\n "+ userRepository.findUserByEmail("admin@mail.com").get().getTokens());
			}

			var manager = RegisterRequest.builder()
					.firstName("manager")
					.lastName("manager")
					.email("manager@mail.com")
					.password("password")
					.role(Role.MANAGER)
					.phone("99999999")
					.build();

			// Check if the user with the same email already exists
			if (userRepository.findUserByEmail(manager.getEmail()).isEmpty()) {
				System.out.println("Manager token: " + service.register(manager).getAccessToken());
			} else {
				//System.out.println("User with email 'manager@mail.com' already exists.\n "+ userRepository.findUserByEmail("manager@mail.com").get().getTokens());
			}
		};
	}
}
