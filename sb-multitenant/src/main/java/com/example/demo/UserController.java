package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantIdentifierResolver tenantIdentifierResolver;

    @GetMapping
    public List<User> getAllUsers(@RequestHeader("X-TenantID") String tenant) {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@RequestHeader("X-TenantID") String tenant, @PathVariable Long id) {
        tenantIdentifierResolver.setCurrentTenant(tenant);
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping
    public User createUser(@RequestHeader("X-TenantID") String tenant, @RequestBody User user) {
        tenantIdentifierResolver.setCurrentTenant(tenant);
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestHeader("X-TenantID") String tenant, @PathVariable Long id, @RequestBody User userDetails) {
        tenantIdentifierResolver.setCurrentTenant(tenant);
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader("X-TenantID") String tenant, @PathVariable Long id) {
        tenantIdentifierResolver.setCurrentTenant(tenant);
        userRepository.deleteById(id);
    }
}