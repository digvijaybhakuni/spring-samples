package com.digvijayb.multitenant;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = MultitenantApplication.class) 
public class MultitenantApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultitenantApplication.class, args);
	}

	@Entity
    @Table(name = "users")
	@Data
    public static class UserEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String username;
        private String email;

        public UserEntity() {}

        public UserEntity(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }

    // Record as a DTO
    public static record User(Long id, String username, String email) {}

    @Service
    public static class UserService {

        @Autowired
        private UserRepository repository;

        @Autowired
        private TransactionTemplate txTemplate;

        // Convert JPA entities to DTOs
        public Collection<User> getUsers() {
            return repository.findAll().stream()
                .map(user -> new User(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
        }

        public Collection<User> getPublicUsers() {
            return txTemplate.execute(tx -> {
//               TenantContext.setCurrentTenant("public");
                return getUsers();
            });
        }
    }

	@RestController
	@RequestMapping("/users")
	public static class UserController {

		@Autowired
		private UserService service;

        @Autowired
        private TenantIdentifierResolver tenantIdentifierResolver;
		
		@GetMapping
		public Collection<User> getUsers() {
			return service.getUsers();
		}

        @GetMapping("public")
        public Collection<User> getPublicUsers() {
            tenantIdentifierResolver.setCurrentTenant("public");
            return service.getUsers();
        }
	}

}
