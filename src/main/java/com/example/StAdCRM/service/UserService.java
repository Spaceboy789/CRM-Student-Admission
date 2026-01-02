package com.example.StAdCRM.service;

import com.example.StAdCRM.dto.LoginRequest;
import com.example.StAdCRM.dto.LoginResponse;
import com.example.StAdCRM.entity.Role;
import com.example.StAdCRM.entity.User;
import com.example.StAdCRM.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(request.getPassword())) {
                // Return success with dummy token
                return new LoginResponse("Login Successful", user.getRole(), "dummy-token-" + UUID.randomUUID(), user.getId());
            }
        }
        throw new RuntimeException("Invalid credentials");
    }

    // Method to seed users if not exist (called from Main)
    public void createUserIfNotExist(String email, String password, Role role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password); // Simple text password for demo
            user.setRole(role);
            userRepository.save(user);
            System.out.println("Seeded user: " + email);
        }
    }
}
